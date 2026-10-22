---
name: feature-com-spec
description: Guide feature work through an approved specification, a small validated implementation plan, incremental implementation, and acceptance/regression testing. Use when a feature should be developed spec-first with reviewable commits.
---

# Feature Development with a Specification

## Objective

Implement a feature through a reviewed specification and an actionable plan before changing application code. Work incrementally, validate each task, preserve behavior outside the agreed scope, and finish by checking the acceptance criteria and regressions.

This repository is multi-application (`src/` backend, `frontend/` frontend). Determine which runtime(s) are in scope before drafting specs or plans. Do not connect the frontend to the backend, unify their models, or assume shared business rules without an explicit request.

## Inputs

- A concise description of the requested feature and the problem it should solve.
- The target runtime (`src/` backend or `frontend/` frontend) and its existing `AGENTS.md` guidance.
- Any known constraints, stakeholders, deadlines, or platform requirements.

Clarify ambiguous product behavior before writing the specification. Ask focused questions about users, workflows, business rules, edge cases, compatibility, and what is explicitly excluded. Do not invent decisions that materially change scope. Record confirmed decisions in the specification.

## Workflow

### 1. Explore the project and establish guidance

1. Inspect the repository structure, `AGENTS.md`, relevant implementation, tests, and documentation. Start with the feature's likely ownership area (`src/` or `frontend/`) and follow nearby patterns.
2. Identify the application boundary: backend (`src/main/groovy/`, Spock tests, PostgreSQL) or frontend (`frontend/scripts/`, TypeScript/HTML, `localStorage`). Confirm which is in scope.
3. Review the guidance for accuracy. Correct assumptions against source files and executable project configuration; distinguish confirmed facts from unknowns. Preserve useful existing instructions instead of replacing them wholesale.
4. Note anonymity constraints: candidates must not learn which company owns a vacancy; companies must not learn identifying information of candidates who liked their vacancies. Apply anonymity at the data/API boundary, not only in views. Full records may be accessed only in admin contexts.

### 2. Write and approve the feature specification

1. Ask the user targeted questions when needed, then draft the specification at `docs/specs/<nome-da-feature>/spec.md`.
2. Include the context and objective, business rules and edge cases, acceptance criteria (preferably Given/When/Then), impacts on existing behavior, and explicit in-scope and out-of-scope boundaries.
3. Capture finalized decisions: data ownership (PostgreSQL for backend, `localStorage` for frontend), persistence, compatibility, interaction behavior, failure handling, and anonymity rules.
4. Review the specification with the user. Resolve contradictions, vague acceptance criteria, and unaddressed edge cases before implementation.
5. Commit the approved specification before changing application code. If project policy or the user prohibits commits, ask how to proceed and record that exception.

**Do not write feature code before the specification is approved.** Documentation edits needed to clarify or approve the specification are allowed; implementation code is not.

### 3. Create and review the implementation plan

1. Read the approved specification at `docs/specs/<nome-da-feature>/spec.md` and `AGENTS.md` together. Propose a numbered sequence of small, independently verifiable tasks in `docs/specs/<nome-da-feature>/plan.md`.
2. Derive tasks directly from the spec's acceptance criteria. Each task must be small enough to be analyzed, tested, and validated sequentially.
3. For each task, state:
   - What will be done and which spec requirement it advances.
   - Which files or components are expected to change (`src/` or `frontend/` only, not both unless explicitly requested).
   - The focused automated test or concrete manual check that proves it is done (Spock for backend; manual/visual for frontend since no test framework is configured).
4. Order tasks by dependency and risk. Separate oversized tasks until each has a clear boundary and a practical validation check.
5. Review the plan with the user. Confirm it covers every acceptance criterion without expanding scope, and adjust task size or order before coding.

### 4. Implement incrementally, one task at a time

For each numbered task, in order:

1. Mark only the current task in progress and reread its plan entry and related specification rules.
2. Inspect the current code and tests at the owning boundary (`src/` or `frontend/`). Make the smallest change that completes this task; do not bundle unrelated cleanup or later tasks.
3. Read the resulting diff and verify that each change matches the specification and stays within scope. Preserve pre-existing user changes. Preserve Portuguese naming/style conventions (`PascalCase` classes, `snake_case` fields/methods where existing).
4. Run the task's focused automated check first (Spock for backend; build/serve for frontend). Then run broader relevant tests or launch the application. Fix failures in the current task before proceeding.
5. Mark the task complete in the plan only after its validation succeeds. Commit the completed task with a clear message. Do not combine multiple plan tasks into one commit.
6. Move to the next task only after the current task is reviewed, validated, recorded, and committed.

If implementation reveals that the specification is incomplete or incorrect, stop feature coding, update and approve the specification first, and commit the approved specification change before resuming. Update the plan so it remains consistent.

### 5. Validate acceptance and regressions

1. Run the automated tests for the feature using the project's existing test framework: Spock (`./gradlew test`) for backend; no configured frontend test suite exists, so rely on build (`npm run build`) and manual checks for frontend.
2. Check every acceptance criterion individually and report whether it passed, failed, or could not be verified. Link each criterion to a test or a specific manual check.
3. Verify the anonymity principle explicitly for every candidate- and company-facing flow: a candidate must not learn which company owns a vacancy, and a company must not learn the name or personal identifying information of a candidate who liked one of its vacancies. Validate this at the data/API boundary, not only by hiding text in a view or UI.
4. Exercise relevant existing workflows manually in the running application, including behaviors identified in the specification's impact section. For backend: list, filter, like/match flows; for frontend: registration, list/affinity views.
5. Run the broadest relevant regression suite available (`./gradlew test` for backend; `npm run build` + serve for frontend). Report commands and outcomes, including unavailable tests or environment limitations. Do not claim manual validation that was not performed.
6. Review the final diff and repository status to ensure the implementation, tests, documentation (`docs/specs/`), and completed plan agree with the approved scope.

## Rules

- The approved specification at `docs/specs/<nome-da-feature>/spec.md` is the contract for implementation. Resolve product ambiguity before coding.
- No feature code before specification approval.
- Implement one planned task at a time; validate, update the plan, and create one clear commit per completed task.
- If the specification needs to change, update and approve it before changing code to match.
- Do not alter existing behavior or files outside the feature's agreed scope without approval. Follow existing architecture (`model/DAO/service/controller/view` for backend; `scripts/model/service/persistencia/view` for frontend) and conventions.
- Never discard uncommitted or pre-existing user changes.
- Apply anonymity at the data/API boundary for candidate- and company-facing flows, not only by hiding text in a view. Do not expose identifying fields through serialized objects, routes, logs, or client-side data accessible to the other party.
- Do not connect the frontend to the backend, unify their models, or assume shared business rules without an explicit request.
- Prefer focused automated checks first, followed by broader tests and relevant manual regression checks.
- Keep documentation paths (`docs/specs/<nome-da-feature>`) and commands appropriate to the target runtime.

## Templates

### `docs/specs/<nome-da-feature>/spec.md`

```markdown
# <Feature name>

## Context and objective
- What problem exists today?
- Who is affected, and what outcome should the feature provide?
- Target runtime: backend (`src/`) or frontend (`frontend/`)

## Business rules and edge cases
- Rules:
- Invalid or boundary inputs:
- Failure, recovery, or compatibility behavior:
- Confirmed decisions (anonymity, admin access, persistence):

## Acceptance criteria
- Given <initial context>, when <action>, then <observable result>.
- Given <edge case>, when <action>, then <expected result>.

## Impacts on existing behavior
- Affected features, interfaces, data, integrations, and regression risks:
- Existing behavior that must remain unchanged:

## Scope
### In scope
- ...

### Out of scope
- ...
```

### `docs/specs/<nome-da-feature>/plan.md`

```markdown
# <Feature name> Implementation Plan

Implements the approved specification at `docs/specs/<nome-da-feature>/spec.md`. Relevant project guidance: `AGENTS.md`.

Target runtime: backend (`src/`) or frontend (`frontend/`).

## Tasks

### 1. [ ] <Small, outcome-oriented task>
- **What:** <implementation outcome and linked spec requirement>
- **Files:** <files expected to change or be created (e.g., `src/main/groovy/...` or `frontend/scripts/...`)>
- **Done check:** <focused test command (Spock / `npm run build`) and/or concrete manual observation>

### 2. [ ] <Next task>
- **What:** ...
- **Files:** ...
- **Done check:** ...
```
