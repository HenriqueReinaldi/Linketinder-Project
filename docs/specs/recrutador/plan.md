# Recrutador (Backend) Implementation Plan

Implements the approved specification at `docs/specs/recrutador/spec.md`, following `AGENTS.md` and `skills/feature-com-spec/SKILL.md`.

**Status:** aprovado para implementação. A especificação foi revisada e aprovada; a implementação pode começar conforme as fases abaixo.

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

### 0. [ x ] Prepare schema changes and migrate existing vacancies
- **What:** Author SQL to create `nicho` and `recrutador`, add the required `nicho_id` column and foreign key to the existing `vaga` table, seed the unique `geral` niche, and backfill every existing vacancy's `vaga.nicho_id` to `geral` without changing other vacancy data. Update schema/seed snapshots. Provide exact inspection and application commands; never execute database commands.
- **Files:** `banco_de_dados/schema.sql`, `banco_de_dados/entrega.sql`, a standalone migration under `banco_de_dados/migrations/`, and a companion runbook.
- **Done check:** Review SQL and runbook statically for consistency and safe ordering. The user applies the migration to a scratch database and verifies `vaga.nicho_id` exists and references `nicho.id`, vacancy IDs/count and other fields are unchanged, all pre-existing vacancies reference `geral`, and constraints reject missing/unknown niches and duplicate niche names. Do not start Phase 1 until the user reports successful verification.

## Implementation Phases

Before each phase, inspect the closest existing implementations and tests, and follow or extend their patterns wherever possible. For example, review the existing controllers and controller specs before creating a recruiter controller. Keep each layer's changes and tests separate. Domain controllers are not HTTP endpoints; route handling and JSON serialization belong only to Phase 5. Start these phases only after Phase 0 is user-verified.

### 1. [x] Model
- **What:** Add the `Recrutador` domain model and represent niche as a plain `String` on `Recrutador` and `Vaga`, following the existing `Endereco.pais` pattern. Do not create a backend `Nicho` entity/model; niche lookup and relational ID mapping belong to the DAO. Keep validation and representation behavior consistent with the approved spec, including never exposing recruiter passwords. Do not add DAO or workflow logic here.
- **Files:** `src/main/groovy/org/linketinder/model/objetos/`, `src/main/groovy/org/linketinder/model/`, corresponding model specs under `src/test/groovy/org/linketinder/model/`.
- **Done check:** Focused Spock model specs cover required fields, niche string values, allowed representations, and password omission; existing `Vaga` model specs pass. No `Nicho` model is introduced.

### 2. [x] View and controller
- **What:** Add domain-controller operations for the approved recruiter workflows: provisioning administration, recruiter-scoped vacancy listing, and responding to an eligible existing like. Keep request-to-domain assembly and controller results separate from persistence and HTTP routing. Update the existing view layer to match the new recruiter controller contract: any affected shared/terminal view must capture the updated input fields and present the recruiter-specific data flow in the same pattern as the current terminal/shared views; do not create a new interaction path or treat view-level masking as privacy enforcement.
- **Files:** `src/main/groovy/org/linketinder/controller/`, `src/main/groovy/org/linketinder/view/shared/`, `src/main/groovy/org/linketinder/view/terminal/` only if an existing view needs adaptation, controller specs under `src/test/groovy/org/linketinder/controller/`, and view specs under `src/test/groovy/org/linketinder/view/` for any changed view behavior.
- **Done check:** Focused controller tests verify input assembly, delegation and failure behavior using service doubles; any changed view test confirms it presents the updated recruiter flow and the data expected by the controller, without implying a privacy boundary. No HTTP routes or serialization changes in this phase.

### 3. [x] Service
- **What:** Implement the business workflows behind controller operations: recruiter provisioning validation, vacancy filtering by company and niche (`geral` semantics), prohibition of vacancy writes by recruiters, and eligibility/idempotency rules for responding to existing likes. Keep persistence calls behind DAO collaborators and test service behavior with DAO doubles.
- **Files:** `src/main/groovy/org/linketinder/service/`, service specs under `src/test/groovy/org/linketinder/service/`, and service bundle wiring where required.
- **Done check:** Focused Spock service specs cover valid and rejected inputs, company/niche boundaries, absent prior likes, idempotent responses, and no mutation on rejected operations. No SQL implementation in this phase.

### 4. [x] DAO
- **What:** Implement persistence and hydration for recruiters, map niche strings on recruiter/vacancy models to the database's `nicho` table and `nicho_id` foreign keys, support recruiter-scoped vacancy queries, and implement eligibility-checked updates to existing likes. Match the service collaborator contracts from Phase 3 and keep SQL/data mapping out of services and controllers. Do not add a `Nicho` domain entity.
- **Files:** `src/main/groovy/org/linketinder/DAO/` and the existing relevant service/controller specs that already cover the DAO behavior via mocks and integration-level contracts.
- **Done check:** Run the existing targeted Spock tests for recruiter and like-related DAO/service contracts; verify the recruiter persistence, company/niche filtering, and existing-like update behavior through the existing suite without creating new DAO-specific specs. Tests must not connect to or mutate a real database.

### 5. [x] Endpoints
- **What:** Wire the approved operations through HTTP handlers and application bundles. Define/implement route and method contracts separately from domain controllers, and serialize explicit audience-appropriate response shapes: omit candidate identifiers from recruiter/company-facing payloads, omit company/recruiter identity from candidate-facing payloads, and never return passwords. Preserve the spec's accepted unfiltered-description risk. Do not imply secure role isolation without approved and implemented access control.
- **Files:** `src/main/groovy/org/linketinder/controller/http/`, `src/main/groovy/org/linketinder/Main.groovy`, and the relevant existing controller/service specs already covering the runtime contract.
- **Done check:** Run the existing focused Spock tests that cover recruiters and related controller behavior, and confirm the HTTP wiring and serialization contract through the project’s current test suite instead of creating new HTTP-only specs. Verify registration/wiring without requiring a live database; document the remaining authentication/authorization limitation.

### 6. [x] Run acceptance and regression checks
- **What:** Verify every acceptance criterion against focused tests and agreed migration/manual checks; review the final diff and ensure only backend and spec-plan scope changed. Database inspection/application remains user-run; record results only after the user reports them.
- **Files:** Tests and documentation from the phases above; no additional implementation files expected.
- **Done check:** Focused specs pass, then `./gradlew test` passes. Record each acceptance, migration, and privacy check outcome, including checks blocked by unavailable PostgreSQL/access-control infrastructure. Do not run database-mutating commands or claim unperformed user-run checks.

### 7. [x] Recruiter curtida listing
- **What:** Add the recruiter-specific listing of curtidas relevant to the recruiter’s company and niche scope. The list must include only curtidas whose vacancy belongs to the recruiter’s company and whose niche matches the recruiter’s compatibility rule, with `geral` allowing any niche within the same company. This expands the approved recruiter workflow without altering the earlier steps or the existing backlog order.
- **Files:** `src/main/groovy/org/linketinder/view/`, `src/main/groovy/org/linketinder/controller/`, `src/main/groovy/org/linketinder/service/`, `src/main/groovy/org/linketinder/DAO/`, and the existing recruiter/curtida specs in `src/test/groovy/org/linketinder/`.
- **Done check:** Run the existing focused Spock tests covering recruiter and curtida behavior, and verify the list is filtered by company and niche compatibility using the project’s current test suite rather than creating new DAO or HTTP-only specs.

## Frontend implementation phases

The frontend work follows the same backend logic and keeps the existing browser-only architecture: recruiters are selected by ID in a recruiter view, and candidates can interact with vacancies through a like action in the candidate view. These steps are additive and should be implemented after the backend phases are stable.

### 8. [x] Frontend recruiter domain model and browser data plumbing
- **What:** Extend the frontend model layer with the recruiter representation required by the recruiter view, including recruiter ID, company relation, niche, and the minimum data needed to hydrate the list of matching curtidas. Preserve the existing TypeScript patterns under `frontend/scripts/model/`, `frontend/scripts/service/` and `frontend/scripts/persistencia/` without connecting the frontend to the backend API.
- **Files:** `frontend/scripts/model/`, `frontend/scripts/service/` `frontend/scripts/persistencia/`, and any model/service wiring affected by recruiter selection and curtida matching data.
- **Done check:** Existing browser-side models and persistence logic still load without breaking the current frontend build; recruiter data is available in memory/localStorage through the same patterns used by candidatos, empresas, and vagas.

### 9. [x] Frontend recruiter view: `visao recrutador`
- **What:** Implement a recruiter-focused frontend view that allows selecting a recruiter by ID and then listing the curtidas that match the selected recruiter’s company and niche scope. The logic must mirror the backend rule: only curtidas from vagas owned by the recruiter’s company and compatible with the recruiter’s niche are shown, with `geral` allowing any niche within that company. The recruiter view must not imply auth or a protected route; it is simply a browser-driven selection and display flow.
- **Files:** `frontend/scripts/view/`, `frontend/view/`, and the related service/controller-layer TypeScript modules used to compose the view.
- **Done check:** The browser UI can select a recruiter by ID and render only matching curtidas; same-company and niche filters are enforced in the frontend logic, and the view is consistent with the backend business rule.

### 10. [x] Frontend candidate like workflow for vagas and recruiter response flow
- **What:** Add the candidate-side feature that lets a candidate like a vacancy and extend the same localStorage-backed flow so a recruiter can like a candidate back when the recruiter has a compatible vacancy match. The candidate should be able to select a candidate record and, from a vacancy list or detail view, create a curtida entry for a selected vaga. The recruiter view should also be able to mark a compatible curtida as `empresa_curtiu = true` when the company responds positively to an existing candidate like. Keep the logic aligned with the backend model: likes must reference the candidate and the vacancy, duplicate likes for the same pair should be prevented or treated idempotently in the frontend state, and a recruiter response should update the existing curtida row rather than creating a second one.
- **Files:** `frontend/scripts/service/`, `frontend/scripts/view/`, and the data persistence layer for candidate-vacancy and recruiter-candidate interaction state.
- **Done check:** A candidate can like a vacancy through the browser UI, a recruiter can like a candidate back from the compatible curtida list, the like/response is recorded in the managed browser state, repeated likes for the same candidate/vaga pair do not produce duplicate entries, and existing curtidas are updated in place when `empresa_curtiu` becomes true.

### 11. [x] Frontend integration and validation of recruiter + candidate flows
- **What:** Connect the recruiter and candidate screens into the same frontend experience so the user can move among recruiter selection, recruiter curtida listing, and candidate vacancy likes without breaking the existing static pages. Ensure the data flow remains localStorage-based and that each screen reads only the fields necessary for the feature, without exposing more identity than the approved business boundary allows.
- **Files:** `frontend/scripts/main.ts`, `frontend/scripts/view/`, `frontend/scripts/service/`, and any affected HTML templates/styles.
- **Done check:** The frontend builds correctly with the added recruiter and like flows, and the interactions follow the same logic as the backend rules: recruiter views only same-company compatible curtidas, and candidate likes are stored as candidate-vaga relationships.

### 12. [x] Frontend recruiter registration flow
- **What:** Add a frontend recruiter creation flow parallel to the existing candidate and company registration flows. This should allow creating a new recruiter in the browser with the required fields (at least name, contact/email, CPF, date of birth, password, company association, and niche), validate the same basic rules already used by other frontend forms, and persist the created recruiter into the shared localStorage data model. This task is separate from the backend/recruiter API and should not assume any server connection.
- **Files:** `frontend/scripts/service/`, `frontend/scripts/view/`, `frontend/view/`, and any affected registration form or data-persistence wiring.
- **Done check:** A recruiter can be created through the frontend form, the new recruiter appears in the same localStorage-backed list as other recruiters, the form rejects invalid or incomplete input, and the flow remains consistent with the existing candidate/company browser registration patterns.

### 13. [x] Frontend acceptance and regression checks
- **What:** Run the existing frontend build and smoke-check the new screens to validate that the recruiter selection, curtida listing, and candidate like flows operate together without regressing the earlier frontend pages. This is a browser-only verification step and does not replace backend validation.
- **Files:** `frontend/` source and generated build output; no new backend artifacts or database changes.
- **Done check:** `npm run build` succeeds, the static pages still render, and the new recruiter/candidate flows are consistent with the approved logic and the project’s current localStorage-based frontend architecture.

### 14. [ ] Apply SHA-512 password policy to recruiter service
- **What:** Update recruiter provisioning in the backend service layer to reject submitted passwords shorter than six characters and store only the lowercase hexadecimal SHA-512 digest of the password's UTF-8 bytes, as defined in `spec.md`. Apply this policy only to recruiters; do not change candidate or company password handling.
- **Files:** Recruiter service implementation under `src/main/groovy/org/linketinder/service/` and focused recruiter service specs under `src/test/groovy/org/linketinder/service/`. No DAO, model, controller/HTTP, frontend, or database changes.
- **Done check:** Focused Spock tests verify the digest against a known SHA-512 value, minimum-length rejection before persistence, and absence of plaintext in the recruiter object passed to the DAO. Existing candidate and company service tests pass unchanged, confirming their password behavior was not modified.

## Delivery workflow

Important: only the user may mark tasks as done. The assistant must not update checklist items, phase completion markers, or status fields to reflect completion without explicit user confirmation. The plan is a tracking aid for review, not a place for the assistant to self-approve work.

Approve the specification decisions first, then complete and obtain user verification of Phase 0 before starting Phase 1. Implement one phase at a time. Run that phase's focused check before moving on, update its checkbox only after validation passes, and keep each phase's changes independently reviewable. Do not begin the frontend or connect it to the backend as part of this plan.