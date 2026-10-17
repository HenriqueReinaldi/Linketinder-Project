# Recrutador (Backend) Implementation Plan

Implements the proposed specification at `docs/specs/recrutador/spec.md`, following `AGENTS.md` and `skills/feature-com-spec/SKILL.md`.

**Status:** proposta para revisão. A especificação ainda está marcada como rascunho e precisa ser aprovada antes da implementação.

Target runtime: backend (`src/` and `banco_de_dados/`). The frontend remains out of scope.

## Implementation gates

Resolve and record these decisions in the specification before starting application-code tasks:

- Define recruiter password storage, hashing dependency/algorithm, and compatibility expectations. Do not copy the current plaintext storage pattern.
- Reconcile administrative-only recruiter provisioning with the current absence of authentication/authorization and the spec's exclusion of authorization. Do not expose an unauthenticated administrative operation as though it were protected.
- Define route/response contracts that enforce candidate-facing and recruiter-facing anonymity at the HTTP boundary. Current routes do not authenticate or identify the caller, so separate response shapes alone must not be presented as secure access control.

Database changes are user-run only. The implementation may author SQL and exact inspection/application commands in files, but must not connect to the database or run those commands. The repository has schema snapshots but no established migration runner/history, so this plan uses a standalone migration SQL file and a companion instruction file; the user reviews and executes them.

## Prerequisites

Resolve and approve the specification decisions before implementation begins. Schema work is then the first implementation phase and must be completed and user-verified before any application-code phase.

### Approve specification decisions
- **What:** Resolve the implementation gates above, confirm the route/access model and privacy requirements, and approve the spec. Keep the accepted free-text description risk and user-run database process explicit.
- **Files:** `docs/specs/recrutador/spec.md`
- **Done check:** User review confirms the decisions and acceptance criteria; the spec is marked approved. No application code changes.

## Phase 0: Database schema

### 0. [ ] Prepare schema changes and migrate existing vacancies
- **What:** Author SQL to create `nicho` and `recrutador`, add the required `nicho_id` column and foreign key to the existing `vaga` table, seed the unique `geral` niche, and backfill every existing vacancy's `vaga.nicho_id` to `geral` without changing other vacancy data. Update schema/seed snapshots. Provide exact inspection and application commands; never execute database commands.
- **Files:** `banco_de_dados/schema.sql`, `banco_de_dados/entrega.sql`, a standalone migration under `banco_de_dados/migrations/`, and a companion runbook.
- **Done check:** Review SQL and runbook statically for consistency and safe ordering. The user applies the migration to a scratch database and verifies `vaga.nicho_id` exists and references `nicho.id`, vacancy IDs/count and other fields are unchanged, all pre-existing vacancies reference `geral`, and constraints reject missing/unknown niches and duplicate niche names. Do not start Phase 1 until the user reports successful verification.

## Implementation Phases

Before each phase, inspect the closest existing implementations and tests, and follow or extend their patterns wherever possible. For example, review the existing controllers and controller specs before creating a recruiter controller. Keep each layer's changes and tests separate. Domain controllers are not HTTP endpoints; route handling and JSON serialization belong only to Phase 5. Start these phases only after Phase 0 is user-verified.

### 1. [ ] Model
- **What:** Add `Nicho` and `Recrutador` domain models and the `nicho` relationship on `Vaga`. Keep validation and representation behavior consistent with the approved spec, including never exposing recruiter passwords. Do not add DAO or workflow logic here.
- **Files:** `src/main/groovy/org/linketinder/model/objetos/`, `src/main/groovy/org/linketinder/model/`, corresponding model specs under `src/test/groovy/org/linketinder/model/`.
- **Done check:** Focused Spock model specs cover required fields, relationships, allowed representations, and password omission; existing `Vaga` model specs pass.

### 2. [ ] View and controller
- **What:** Add domain-controller operations for the approved recruiter workflows: provisioning administration, recruiter-scoped vacancy listing, and responding to an eligible existing like. Keep request-to-domain assembly and controller results separate from persistence and HTTP routing. Update a view only where an existing shared/terminal view is in scope; do not create a new interaction path or treat view-level masking as privacy enforcement.
- **Files:** `src/main/groovy/org/linketinder/controller/`, `src/main/groovy/org/linketinder/view/` only if an existing view needs adaptation, controller specs under `src/test/groovy/org/linketinder/controller/`, and view specs only for changed views.
- **Done check:** Focused controller tests verify input assembly, delegation and failure behavior using service doubles; any changed view test confirms it presents only the approved view data. No HTTP routes or serialization changes in this phase.

### 3. [ ] Service
- **What:** Implement the business workflows behind controller operations: recruiter provisioning validation, vacancy filtering by company and niche (`geral` semantics), prohibition of vacancy writes by recruiters, and eligibility/idempotency rules for responding to existing likes. Keep persistence calls behind DAO collaborators and test service behavior with DAO doubles.
- **Files:** `src/main/groovy/org/linketinder/service/`, service specs under `src/test/groovy/org/linketinder/service/`, and service bundle wiring where required.
- **Done check:** Focused Spock service specs cover valid and rejected inputs, company/niche boundaries, absent prior likes, idempotent responses, and no mutation on rejected operations. No SQL implementation in this phase.

### 4. [ ] DAO
- **What:** Implement persistence and hydration for recruiter/niche, vacancy niche assignment and recruiter-scoped vacancy queries, and eligibility-checked updates to existing likes. Match the service collaborator contracts from Phase 3 and keep SQL/data mapping out of services and controllers.
- **Files:** `src/main/groovy/org/linketinder/DAO/`, DAO specs under `src/test/groovy/org/linketinder/DAO/`, plus connection-free SQL/mapping tests as supported by the existing test setup.
- **Done check:** Focused DAO tests verify SQL parameters/mapping, niche and recruiter persistence, company/niche filtering, existing-like-only updates, and unchanged state for rejected eligibility cases. Tests must not connect to or mutate a real database.

### 5. [ ] Endpoints
- **What:** Wire the approved operations through HTTP handlers and application bundles. Define/implement route and method contracts separately from domain controllers, and serialize explicit audience-appropriate response shapes: omit candidate identifiers from recruiter/company-facing payloads, omit company/recruiter identity from candidate-facing payloads, and never return passwords. Preserve the spec's accepted unfiltered-description risk. Do not imply secure role isolation without approved and implemented access control.
- **Files:** `src/main/groovy/org/linketinder/controller/http/`, `src/main/groovy/org/linketinder/Main.groovy`, relevant controller/service bundles, and focused HTTP tests under `src/test/groovy/`.
- **Done check:** HTTP-boundary tests assert route/method/status behavior and serialized field presence/absence for each approved contract. Verify registration/wiring without requiring a live database; document the remaining authentication/authorization limitation.

### 6. [ ] Run acceptance and regression checks
- **What:** Verify every acceptance criterion against focused tests and agreed migration/manual checks; review the final diff and ensure only backend and spec-plan scope changed. Database inspection/application remains user-run; record results only after the user reports them.
- **Files:** Tests and documentation from the phases above; no additional implementation files expected.
- **Done check:** Focused specs pass, then `./gradlew test` passes. Record each acceptance, migration, and privacy check outcome, including checks blocked by unavailable PostgreSQL/access-control infrastructure. Do not run database-mutating commands or claim unperformed user-run checks.

## Delivery workflow

Approve the specification decisions first, then complete and obtain user verification of Phase 0 before starting Phase 1. Implement one phase at a time. Run that phase's focused check before moving on, update its checkbox only after validation passes, and keep each phase's changes independently reviewable. Do not begin the frontend or connect it to the backend as part of this plan.