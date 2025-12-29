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
