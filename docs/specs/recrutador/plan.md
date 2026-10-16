# Recrutador (Backend) Implementation Plan

Implements the proposed specification at `docs/specs/recrutador/spec.md`, following `AGENTS.md` and `skills/feature-com-spec/SKILL.md`.

**Status:** proposta para revisão. A especificação ainda está marcada como rascunho e precisa ser aprovada antes da implementação.

Target runtime: backend (`src/` and `banco_de_dados/`). The frontend remains out of scope.

## Implementation gates

Resolve and record these decisions in the specification before starting application-code tasks:

- Define recruiter password storage, hashing dependency/algorithm, and compatibility expectations. Do not copy the current plaintext storage pattern.
- Reconcile administrative-only recruiter provisioning with the current absence of authentication/authorization and the spec's exclusion of authorization. Do not expose an unauthenticated administrative operation as though it were protected.
- Define route/response contracts that enforce candidate-facing and recruiter-facing anonymity at the HTTP boundary. Current routes do not authenticate or identify the caller, so separate response shapes alone must not be presented as secure access control.
- Choose how database changes are delivered and applied. The repository has schema snapshots but no established migration runner/history.

## Tasks

### 1. [ ] Resolve decisions and approve the specification
- **What:** Resolve the implementation gates above, ensure the privacy requirements are compatible with the agreed route/access model, and update the spec's status and rules. Keep the accepted free-text description risk explicit.
- **Files:** `docs/specs/recrutador/spec.md`
- **Done check:** User review confirms the decisions and acceptance criteria; the spec is marked approved. No application code changes in this task.

### 2. [ ] Add schema changes and migrate existing vacancies
- **What:** Add `nicho` and `recrutador`, seed the unique `geral` niche, add required niche references, and backfill every existing vacancy to `geral` without changing its other data. Apply the migration delivery strategy agreed in Task 1 and keep the schema/seed snapshots consistent.
- **Files:** `banco_de_dados/schema.sql`, `banco_de_dados/entrega.sql`, and the migration artifact/run instructions selected in Task 1.
- **Done check:** Apply the migration to a scratch database with the pre-change schema and existing vacancies; verify vacancy IDs/count and other fields are unchanged, every old vacancy references `geral`, and constraints reject missing/unknown niches and duplicate niche names.

### 3. [ ] Implement niche and recruiter domain persistence
- **What:** Add domain models and DAO operations for niches and recruiters, including required-field, company/niche-reference, duplicate-CPF, and six-character password validation. Keep secrets out of returned models and logs according to the approved storage policy.
- **Files:** `src/main/groovy/org/linketinder/model/objetos/`, `src/main/groovy/org/linketinder/DAO/`, corresponding `src/test/groovy/org/linketinder/model/` and `src/test/groovy/org/linketinder/DAO/` specs.
- **Done check:** Focused Spock specs cover valid records, missing fields/references, duplicate CPF/niche constraints, short passwords, and absence of password values from exposed representations.

### 4. [ ] Add administrative recruiter provisioning workflow
- **What:** Implement recruiter provisioning, updates, and deactivation only to the extent approved by Task 1, and wire the model/service/controller through the application bundles. Preserve the agreed administrative access boundary; do not add vacancy-management permissions.
- **Files:** `src/main/groovy/org/linketinder/service/`, `src/main/groovy/org/linketinder/controller/`, `src/main/groovy/org/linketinder/Main.groovy`, `src/main/groovy/org/linketinder/service/ServiceBundle.groovy`, `src/main/groovy/org/linketinder/controller/ControllerBundle.groovy`, plus focused service/controller specs under `src/test/groovy/`.
- **Done check:** Focused Spock tests verify provisioning validation, agreed access behavior, and that responses/logged output never contain the recruiter password.

### 5. [ ] Add niche support to vacancy create and update
- **What:** Carry `nicho_id` through vacancy model, DAO, service, and controller. Require an existing niche on create/update, while preserving existing vacancy behavior apart from the new required classification.
- **Files:** `src/main/groovy/org/linketinder/model/objetos/Vaga.groovy`, `src/main/groovy/org/linketinder/DAO/VagaDAO.groovy`, `src/main/groovy/org/linketinder/service/VagaService.groovy`, `src/main/groovy/org/linketinder/controller/VagaController.groovy`, and corresponding `Vaga`/controller/service specs under `src/test/groovy/`.
- **Done check:** Focused Spock tests verify niche persistence and rejection of missing or nonexistent niches on create/update; existing vacancy tests continue to pass.

### 6. [ ] Add recruiter-scoped, read-only vacancy listing
- **What:** Resolve the recruiter’s associated company and list only that company’s vacancies. A specific recruiter sees exact niche matches; a `geral` recruiter sees all niches for that company. Recruiter-facing operations must not create, edit, delete, or transfer vacancies.
- **Files:** `src/main/groovy/org/linketinder/DAO/VagaDAO.groovy`, `src/main/groovy/org/linketinder/service/VagaService.groovy`, `src/main/groovy/org/linketinder/controller/VagaController.groovy`, and focused vacancy service/controller specs under `src/test/groovy/`.
- **Done check:** Focused Spock tests cover same-company/same-niche results, `geral` results, exclusion of other-company vacancies, invalid recruiter handling, and unchanged vacancy data after attempted write operations.

### 7. [ ] Enforce eligibility for recruiter responses to likes
- **What:** Add the recruiter response workflow so it only sets `empresa_curtiu=true` on an existing candidate-vacancy like when the vacancy belongs to the recruiter’s company and its niche is compatible. Repeated responses remain idempotent; rejected requests do not create or modify likes.
- **Files:** `src/main/groovy/org/linketinder/DAO/CurtidaDAO.groovy`, `src/main/groovy/org/linketinder/service/CurtidaService.groovy`, `src/main/groovy/org/linketinder/controller/CurtidaController.groovy`, and corresponding `Curtida` service/controller specs under `src/test/groovy/`.
- **Done check:** Focused Spock tests cover eligible response, no prior candidate-like, other company, incompatible niche, idempotent repeat, and no state change on every rejection.

### 8. [ ] Enforce anonymous HTTP response shapes
- **What:** Replace direct serialization of full domain objects on affected candidate/recruiter-facing flows with explicit response projections. Exclude candidate name, surname, age/date of birth, CPF, e-mail, and address from recruiter/company-facing candidate data; exclude company and recruiter identity from candidate-facing vacancy/like results; never return passwords. Competencies may be returned. Preserve and document the spec's accepted unfiltered-description risk.
- **Files:** `src/main/groovy/org/linketinder/controller/http/EntidadeHTTP.groovy`, affected `CandidatoHTTP.groovy`, `VagaHTTP.groovy`, and `CurtidaHTTP.groovy`, plus focused HTTP serialization/controller specs under `src/test/groovy/`.
- **Done check:** HTTP-boundary tests inspect serialized JSON and assert both required field presence and prohibited identifying/password field absence for each approved audience/route contract. Do not claim secure role isolation unless the approved access model actually enforces it.

### 9. [ ] Run acceptance and regression checks
- **What:** Verify every acceptance criterion against focused tests and the agreed migration/manual checks; review the final diff and ensure only backend and spec-plan scope changed.
- **Files:** Tests and documentation in the tasks above; no additional implementation files expected.
- **Done check:** Focused specs pass, then `./gradlew test` passes. Record the outcome of each acceptance criterion, migration check, privacy check, and any check blocked by unavailable PostgreSQL/access-control infrastructure. Do not claim unperformed manual checks.

## Delivery workflow

Implement one task at a time after Task 1 is approved. Run that task's focused check before moving on, update its checkbox only after validation passes, and keep each task's changes independently reviewable. Do not begin the frontend or connect it to the backend as part of this plan.