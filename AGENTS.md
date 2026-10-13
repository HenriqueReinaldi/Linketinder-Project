# AGENTS.md

## Overview

Linketinder is a recruitment application inspired by LinkedIn and Tinder. This repository contains two implementations (backend in `src/`, frontend in `frontend/`) that should share the same business rules and logic. Their current separation reflects unfinished development work, not an intentional design choice. The frontend is not currently an API client for the backend.

The application is still in development. Differences between source code, SQL, documentation, and the two projects may reflect unfinished work; do not silently treat an inconsistency as an intentional rule. If an inconsistency causes confusion about the requested change or expected behavior, ask the user before deciding how to proceed.

## Stack

| Layer | Component | Technology |
|-------|-----------|-------------|
| Backend | Main Entry Point | Groovy 5 + Gradle |
| Backend | HTTP Server | JDK HttpServer + Groovy JSON |
| Backend | Database | PostgreSQL |
| Backend | Testing | Spock (JUnit Platform) + Mockito |
| Frontend | Framework | TypeScript + HTML/CSS |
| Frontend | Build / Serve | npm (`tsc` + `serve`) |
| Frontend | Storage | Browser `localStorage` |
| Frontend | Charts | Chart.js (CDN) |
| Database | Scripts | PostgreSQL (`banco_de_dados/`) |

## Structure

- `src/main/groovy/org/linketinder/Main.groovy`: backend composition and startup.
- `src/main/groovy/org/linketinder/model/`: shared person abstraction and domain objects.
- `src/main/groovy/org/linketinder/DAO/`: database access, including connection providers.
- `src/main/groovy/org/linketinder/service/`: application workflows.
- `src/main/groovy/org/linketinder/controller/`: controllers and HTTP handlers.
- `src/main/groovy/org/linketinder/view/`: terminal/shared views; terminal startup is not the active application path.
- `src/test/groovy/`: Spock specifications for backend models, services, controllers, and views.
- `frontend/scripts/model/`, `service/`, `persistencia/`, `view/`: frontend models, workflows, local persistence, and DOM-facing views.
- `frontend/view/`: static HTML pages and stylesheet.
- `frontend/build/`: compiled frontend output. Edit `frontend/scripts/`, then build; do not treat generated output as the source of truth.
- `banco_de_dados/schema.sql`: database schema; `entrega.sql`: schema plus sample data; `buscas.sql`: example queries.
- Root `build/` and `bin/` contain generated/compiled output, not primary source.
- `CHANGELOG.md`: project history file at the root; it contains the changelog and can be useful for tracking changes.
- `skills/`: folder at root containing project skills that the AI model can reference.

## Execute And Test

### Backend

1. Ensure a PostgreSQL database named `linketinder` exists (create it if it does not already exist).
2. Apply `banco_de_dados/entrega.sql` to create and populate its tables (only needed when setting up a new database).
3. Check the connection settings in `src/main/groovy/org/linketinder/DAO/conexoes/ProvedorPostgres.groovy`; connection settings are currently defined in source.
4. From the repository root, run `./gradlew run -q --console=plain`.
5. Run backend tests with `./gradlew test`; compile production sources with `./gradlew classes`.

The HTTP server currently binds to `localhost:3040`. Tests use Spock and commonly mock services or DAOs; a passing test suite does not by itself verify a live PostgreSQL connection or HTTP privacy behavior.

### Frontend

From `frontend/`, run `npm install`, then `npm run this` to compile and serve the static site. The component commands are `npm run build` and `npm start`. The default `serve` address is printed by the command. There is no configured frontend test command; do not report frontend tests as run unless a test suite is added and executed.

## Conventions

- Code follows clean code and SOLID principles.
- Preserve the existing separation of backend model/DAO/service/controller/view responsibilities.
- Existing domain names and many identifiers are Portuguese. Classes use PascalCase; fields and methods often use `snake_case`. Preserve nearby naming and style rather than doing unrelated renames.
- Backend tests are Spock specifications, generally using descriptive feature names and `given`/`when`/`then` blocks.
- Frontend TypeScript uses ES modules and a `NodeNext` module configuration. Follow the existing `.js` import suffix convention in TypeScript imports.
- Keep changes in source directories; regenerate compiled output with the corresponding Gradle or npm command when needed.
- No repository-wide formatter or linter is configured in the inspected build files. Do not invent or claim a lint command.

## Restrictions And Unresolved Decisions

- If a requirement or decision is ambiguous or undocumented, stop and ask the user before choosing.
- **Anonymity is a required business constraint:** a candidate must not learn which company owns a vacancy. A company must not learn the name or personal/identifying information of a candidate who liked one of its vacancies.
- Full candidate/company records may be accessed by administrative endpoints or functions for administrative purposes; this is not a violation of anonymity between candidates and companies. Keep such access strictly in the admin context and do not expose those records to the opposing user role.
- Apply anonymity at the data/API boundary for candidate- and company-facing flows, not only by hiding text in a view. Do not expose identifying fields through serialized objects, routes, logs, or client-side data accessible to the other party.
- The exact set of fields considered personal/identifying, any point at which identity may be revealed, and whether a mutual like permits disclosure are not specified. Stop and ask the user before choosing or implementing any such policy.
- Do not connect the frontend to the backend, unify their models, or assume shared business rules without an explicit request. Their current persistence and workflows differ.
- Do not assume frontend data is stored in PostgreSQL. Conversely, do not treat browser `localStorage` as backend persistence.
- Do not use real credentials in code or documentation. The backend connection configuration currently resides in source; handle any future configuration change without publishing secrets.

## Domain Entities And Relationships

### Backend domain (`src/`)

`Pessoa` is the shared base type for `Candidato` and `Empresa`. A candidate has personal/profile data, an address, and competencies. A company has profile data and can own multiple vacancies. Each vacancy has an address and desired competencies and belongs to one company. A `Curtida` relates one candidate to one vacancy and records whether the company has responded through `empresa_curtiu`; the model has no separate like identifier. `Endereco` and `Competencia` are referenced domain objects.

### Frontend domain (`frontend/`)

The TypeScript `Pessoa` is extended by frontend `Candidato` and `Empresa` models; these are browser models, not the Groovy classes. A frontend `Vaga` contains vacancy information, desired competencies, and an in-memory company reference. Frontend services manage registration and list/affinity views over browser data. They do not currently implement a like workflow or call backend routes.

### Database domain (`banco_de_dados/`)

In PostgreSQL, `candidato.endereco_id` and `empresa.endereco_id` reference `endereco.id`. Each address references `pais.id` through `pais_id` and `estado.id` through `estado_id`. A vacancy stores its owner in `vaga.empresa_id` referencing `empresa.id`, and its location in `vaga.endereco_id` referencing `endereco.id`. Candidate skills and vacancy requirements are many-to-many relationships represented by `candidato_competencias(candidato_id, competencia_id)` and `vaga_competencias(vaga_id, competencia_id)`, each with a composite primary key. The `curtida` table references `candidato.id` through `candidato_id` and `vaga.id` through `vaga_id`, with those two columns forming its composite primary key. The SQL schema is reflected on the backend application.

## Business Rules

### Like and match flow

A candidate liking a vacancy creates a `curtida` record for the `(candidato_id, vaga_id)` pair. The same candidate may like multiple vacancies, but may have only one record for any particular vacancy. If the pair already exists, the backend DAO uses `ON CONFLICT (candidato_id, vaga_id) DO NOTHING`, so another candidate-like does not create a duplicate or change the existing record. The database's composite primary key enforces this pair-level uniqueness.

A match occurs when the company owning a vacancy likes the candidate who already liked that vacancy. This updates that same `curtida` record by setting `empresa_curtiu` to `true`; it does not create a second record. The DAO update locates the row by `candidato_id` and `vaga_id`, so the workflow must ensure the acting company owns that vacancy. The current SQL update itself does not verify company ownership or authorization. `empresa_curtiu` defaults to `false` for newly inserted rows, but the SQL column is nullable because it is not declared `NOT NULL`.

### Anonymity and administrative access

Candidates must not learn which company owns a vacancy, and a company must not learn the name or personal/identifying information of a candidate who liked one of its vacancies. Full candidate or company records may be used by administrative endpoints or functions for administrative purposes; this does not violate anonymity between the two user roles, provided the records remain restricted to the admin context. Candidate- and company-facing output must apply anonymity at the data/API boundary, not merely by hiding names in a view.

The current backend hydrates full company/candidate objects and HTTP handlers serialize domain objects. The registered routes do not currently enforce authentication or admin authorization, so their intended administrative purpose is not, by itself, an access-control boundary. The frontend's visual pseudonym is likewise not a privacy boundary because full candidate objects are stored in browser `localStorage`. Do not assume a full-record route is admin-only unless access control is implemented and verified. The exact set of identifying fields and any point at which identity may be revealed are unspecified; ask the user before choosing such a policy.

### Frontend affinity rules

The frontend's candidate-to-vacancy affinity is based on the candidate's competencies matching a vacancy's desired competencies. Its company-to-candidate affinity weights candidate competencies by how frequently they are requested across that company's vacancies. These calculations belong only to the frontend implementation; they are not backend rules and must not be silently copied between the two independent projects.

## PostgreSQL Schema Rules

The schema gives each entity table a serial `id` primary key, except the association tables, whose composite primary keys prevent duplicate candidate/competency, vacancy/competency, and candidate/vacancy pairs. `pais.nome` and `estado.nome` are unique and required. `endereco.CEP`, `pais_id`, and `estado_id` are required, and the latter two reference `pais.id` and `estado.id`.

Candidate records require `nome`, `sobrenome`, `e_mail`, `CPF`, `descricao`, `data_nascimento`, `senha`, and `endereco_id`. `candidato.CPF` is unique, and `endereco_id` references `endereco.id`. Company records require `nome`, `e_mail`, `CNPJ`, `descricao`, `senha`, and `endereco_id`; `empresa.CNPJ` is unique and its address reference points to `endereco.id`. Both password columns have a `CHECK` requiring at least six characters, but the schema does not hash passwords. Competencies require a unique `tecnologia` value. A vacancy requires `nome`, `descricao`, `endereco_id`, and `empresa_id`; its foreign keys reference `endereco.id` and `empresa.id`.

The `candidato_competencias` and `vaga_competencias` join tables reference their corresponding candidate or vacancy and `competencia.id`. The `curtida` table requires `candidato_id` and `vaga_id`, references `candidato.id` and `vaga.id`, and uses `(vaga_id, candidato_id)` as its primary key. The join rows and curtidas declare `ON DELETE CASCADE` for their referenced candidates, vacancies, or competencies, so deleting a referenced row removes the associated rows. Other foreign keys, including the address and vacancy-owner references, do not declare cascading deletion in this schema.

These SQL constraints do not provide privacy controls: the schema defines no privacy views or database role policies. The frontend model and SQL schema differ, including candidate age versus `data_nascimento` and frontend-only education/salary fields. Follow the development-status guidance above when such differences affect a change.