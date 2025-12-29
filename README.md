# Application supporting antibiotic treatment in hospital

The application analyses antibiotic susceptibility for isolated strains and helps pick empiric therapy for hospitalised patients.

## Stack
Java 17 (Spring Boot), MySQL, React (MUI), Docker/Docker Compose

## Project layout
- `src/main/java` – Spring Boot application (REST API, JWT security, FRAT/trend analytics)
- `src/main/resources` – configuration, templates, static assets (including `robots.txt`)
- `frontend/` – React SPA with protected routes and upload/import flows
- `.github/workflows` – CI for Maven and Node.js

## Prerequisites
- Docker + Docker Compose **or** local MySQL 5.7/8.0 running on `localhost:3306`
- JDK 17 and Maven
- Node 16.x with npm

## Quick start (Docker Compose)
```bash
BACKEND_PORT=8080 FRONTEND_PORT=3000 API_BASE_URL=http://localhost:8080/api docker-compose up --build
```
- Backend available at `http://localhost:8080`
- Frontend (Nginx build) at `http://localhost:3000`

## Local development
### Backend
1. Create database and user (example for local MySQL):
   ```bash
   mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS db_antibiotic;"
   mysql -uroot -p -e "CREATE USER IF NOT EXISTS 'springuser'@'%' IDENTIFIED BY 'ThePassword';"
   mysql -uroot -p -e "GRANT ALL PRIVILEGES ON db_antibiotic.* TO 'springuser'@'%'; FLUSH PRIVILEGES;"
   ```
2. Run the app:
   ```bash
   mvn spring-boot:run
   ```
   (defaults: `spring.profiles.active=prod`, DB URL `jdbc:mysql://localhost:3306/db_antibiotic`).

### Frontend
```bash
cd frontend
npm ci
npm start
```
The dev server runs on `http://localhost:3000` and proxies API calls to `REACT_APP_API_BASE_URL` if set.

## Testing
- Backend: `mvn -DskipTests package` (or `mvn test` once a test DB is available)
- Frontend: `npm test` (watch) or `npm run build` for a production bundle

## CI (GitHub Actions)
- **Java CI with Maven**: provisions MySQL in the workflow and builds the Spring Boot app.
- **Node.js CI**: installs dependencies in `frontend/` and runs `npm run build`.
Workflows live in `.github/workflows/`.

## Robots.txt
- Backend serves `src/main/resources/static/robots.txt` to discourage indexing of API endpoints.
- Frontend bundle also includes `frontend/public/robots.txt` (CRA default). Adjust contents as needed for deployments.

## Troubleshooting
- File watchers limit on Linux: add `fs.inotify.max_user_watches=524288` to `/etc/sysctl.conf`, then run `sudo sysctl -p`.
- If MySQL fails to start in Docker/CI, confirm the port mapping and credentials match `SPRING_DATASOURCE_*`/`MYSQL_*` variables.
- Maven download issues (restricted egress/proxy): set `MAVEN_MIRROR_URL` to an accessible Maven proxy or configure a local `~/.m2/settings.xml` mirror if outbound access is blocked.

## Improvement Plan and TODOs

This plan highlights what can be improved by subsystem, with actionable TODOs for each area. Keep items in GitHub Issues to track ownership and progress.

### Product and Data Quality
Plan: make data imports reliable, explainable, and auditable so clinicians trust outputs.
- [ ] Publish a strict import template with required columns, types, and acceptable value ranges.
- [ ] Validate inputs before processing and return a row-level error report (line number + reason).
- [ ] Add dry-run imports that show warnings without writing to the database.
- [ ] Normalize vocabulary (antibiotics, organisms, wards) using canonical dictionaries.
- [ ] Add duplicate detection and idempotent import handling for repeated uploads.
- [ ] Store import metadata (who/when/source file/version) and expose it in the UI.
- [ ] Track data provenance for derived metrics (link to source records and filters).
- [ ] Provide sample datasets and seeded fixtures for demos and QA.

### Backend API and Security
Plan: tighten security, version the API surface, and standardize error handling.
- [ ] Rotate refresh tokens, store hashed tokens, and enforce absolute token expiry.
- [ ] Add revocation endpoints and invalidate tokens on password change or logout.
- [ ] Add rate limiting on auth endpoints and heavy analytics routes.
- [ ] Add API versioning (`/api/v1`) and deprecate legacy endpoints with a schedule.
- [ ] Replace Springfox with springdoc-openapi and generate up-to-date API docs.
- [ ] Add pagination, sorting, and filtering to list endpoints and document defaults.
- [ ] Standardize validation errors (JSON shape, error codes) across controllers.
- [ ] Add upload limits and MIME type validation for file imports.
- [ ] Add audit logging for sensitive operations (imports, deletions, role changes).

### Frontend UX and Design System
Plan: reduce UI drift and improve confidence in long-running workflows.
- [ ] Consolidate on MUI v5 and remove legacy `@material-ui/*` packages.
- [ ] Upgrade React tooling (CRA v5 or Vite) and align with Node LTS.
- [ ] Centralize API base URL and error handling in a single client module.
- [ ] Add consistent toasts for success/error states (reuse notistack).
- [ ] Add loading, empty, and error states for tables, charts, and import steps.
- [ ] Add file import progress UI with cancel and retry.
- [ ] Add form validation messages with clear recovery guidance.
- [ ] Make layouts responsive and keyboard accessible.
- [ ] Add error boundaries to prevent blank screens on runtime errors.

### Analytics and Reporting
Plan: make insights easier to compare, export, and explain.
- [ ] Add CSV/Excel exports for key reports and include applied filters in headers.
- [ ] Add time range presets and cohort filters (ward, specimen, organism).
- [ ] Add chart export/download flows (PNG/CSV) and annotated legends.
- [ ] Document calculation logic for antibiograms and trend metrics.
- [ ] Profile and optimize heavy queries used by dashboards.
- [ ] Add caching for repeated analytics queries with TTL and invalidation rules.

### Infrastructure and DevOps
Plan: simplify setup and reduce surprises in production.
- [ ] Add health checks to Docker and docker-compose for backend, frontend, and DB.
- [ ] Provide `.env.example` and document all required environment variables.
- [ ] Centralize API base URL config for frontend and containers.
- [ ] Add DB migrations (Flyway or Liquibase) to avoid manual schema updates.
- [ ] Add nightly backups and a restore checklist for MySQL.
- [ ] Pin base images and dependencies for reproducible builds.
- [ ] Add basic monitoring and alerting hooks (metrics, log shipping).

### Testing and Quality
Plan: protect critical workflows with automated tests.
- [ ] Add service and repository tests for core CRUD and analytics paths.
- [ ] Add integration tests using Testcontainers for MySQL.
- [ ] Add API contract tests for auth and import endpoints.
- [ ] Add frontend E2E tests for login, import, and report viewing.
- [ ] Add snapshot tests for critical charts and tables.
- [ ] Add linting/formatting and CI checks for both backend and frontend.
- [ ] Track coverage targets for critical modules and enforce in CI.

### Documentation
Plan: reduce onboarding time and clarify workflows.
- [ ] Document API endpoints, auth flows, and error codes.
- [ ] Add a data import guide with example files and common errors.
- [ ] Add a data dictionary and mapping rules for domain terms.
- [ ] Add a simple architecture diagram (backend, DB, frontend).
- [ ] Add a runbook for deployment, backups, and incident response.

## V1 Roadmap (Draft)

This roadmap is a starting point for a v1.0 release. Adjust timelines once estimates are confirmed.

### Milestone 1: Data Reliability and Security (4-6 weeks)
- Ship strict import schema validation with row-level errors and dry-run mode.
- Implement refresh token rotation, revocation, and standardized validation errors.
- Add upload size limits, MIME validation, and audit logging for data changes.
- **Success Criteria**: repeatable imports with clear errors, no critical auth issues.

### Milestone 2: UX Stability and Tooling (4-6 weeks)
- Consolidate MUI v5 and remove legacy styling dependencies.
- Add consistent toasts, loading states, and error boundaries.
- Upgrade frontend tooling and align Node version with CI.
- **Success Criteria**: smooth workflows on desktop and mobile, fewer UI regressions.

### Milestone 3: Analytics and Performance (4-6 weeks)
- Add exports (CSV/Excel/PNG) with metadata and filter context.
- Add time presets and cohort filters to trend views.
- Profile and optimize heavy queries and add caching where safe.
- **Success Criteria**: reports load quickly and export reliably.

### Milestone 4: Release Readiness (3-4 weeks)
- Add Testcontainers-backed integration tests and E2E coverage for key flows.
- Add `.env.example`, deployment runbook, and basic monitoring hooks.
- Add DB migrations and backup/restore procedures.
- **Success Criteria**: reproducible deploys and green CI on main.

### Assumptions and Dependencies
- Spring Boot upgrade to v3 requires dependency compatibility review.
- React tooling upgrade depends on MUI v5 consolidation.
- Export features depend on finalized report definitions and schemas.

Track progress in GitHub Issues/Projects and link PRs to the TODOs above.
