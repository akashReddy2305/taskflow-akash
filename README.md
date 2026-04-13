# TaskFlow Backend

## 1. Overview

TaskFlow is a backend-only submission for the TaskFlow engineering take-home assignment. It provides JWT-based authentication, project management, task management, PostgreSQL persistence, and migration-managed schema changes.

### Tech Stack

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL 15
- Flyway
- Docker Compose

## 2. Architecture Decisions

I used Spring Boot to ship a complete backend quickly while keeping the code organized by responsibility.

### Structure

- `backend/src/main/java/com/taskflow/backend/controller`
  HTTP endpoints and status codes
- `backend/src/main/java/com/taskflow/backend/service`
  Business logic and authorization rules
- `backend/src/main/java/com/taskflow/backend/repository`
  Data access
- `backend/src/main/java/com/taskflow/backend/security`
  JWT generation and request authentication
- `backend/src/main/java/com/taskflow/backend/exception`
  Centralized error handling
- `backend/src/main/resources/db/migration`
  Flyway migrations and seed data

### Tradeoffs

- The assignment prefers Go, but I used Java/Spring Boot and documented that choice here.
- I used JPA for repository ergonomics, while keeping schema creation and evolution in SQL migrations as required.
- I kept the API surface intentionally close to the prompt instead of building extra abstraction.

### What I intentionally kept simple

- No pagination yet
- No stats endpoint yet
- No refresh token flow
- No advanced audit logging yet

## 3. Running Locally

Assumption: Docker and Docker Compose are installed.

```bash
git clone https://github.com/your-name/taskflow-your-name.git
cd taskflow
cp .env.example .env
docker compose up --build
```

Services:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

### Clean reset

If you want a fully clean database state:

```bash
docker compose down -v
docker compose up --build
```

The `-v` is helpful when an old local Postgres volume exists and you want Flyway to recreate the schema and seed data from scratch.

## 4. Running Migrations

Flyway runs automatically during backend startup. No manual migration step is required during normal local setup.

Migration files:

- `backend/src/main/resources/db/migration/V1__create_tables.sql`
- `backend/src/main/resources/db/migration/V2__seed_data.sql`

Undo scripts:

- `backend/src/main/resources/db/undo/U1__drop_tables.sql`
- `backend/src/main/resources/db/undo/U2__remove_seed_data.sql`

## 5. Test Credentials

The seed data creates the following test user:

```txt
Email:    test@example.com
Password: password123
```

## 6. API Reference

Base URL:

```txt
http://localhost:8080
```

All non-auth routes require:

```txt
Authorization: Bearer <token>
```

### Auth

#### `POST /auth/register`

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "secret123"
}
```

#### `POST /auth/login`

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

Example response:

```json
{
  "accessToken": "<jwt>"
}
```

### Projects

#### `GET /projects`

Lists projects the authenticated user owns or has assigned tasks in.

#### `POST /projects`

```json
{
  "name": "New Project",
  "description": "Optional description"
}
```

#### `GET /projects/:id`

Returns project details and tasks.

#### `PATCH /projects/:id`

```json
{
  "name": "Updated Project Name",
  "description": "Updated description"
}
```

#### `DELETE /projects/:id`

Returns `204 No Content`.

### Tasks

#### `GET /projects/:id/tasks`

Optional filters:

- `status`
- `assignee`

Example:

```txt
GET /projects/:id/tasks?status=todo&assignee=<user-id>
```

#### `POST /projects/:id/tasks`

```json
{
  "title": "Design homepage",
  "description": "Optional description",
  "priority": "high",
  "assignee_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "due_date": "2026-04-20"
}
```

#### `PATCH /tasks/:id`

```json
{
  "title": "Updated title",
  "description": "Updated description",
  "status": "done",
  "priority": "medium",
  "assignee_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "due_date": "2026-04-22"
}
```

#### `DELETE /tasks/:id`

Returns `204 No Content`.

### Error Format

Validation error:

```json
{
  "error": "validation failed",
  "fields": {
    "email": "is required"
  }
}
```

Unauthorized:

```json
{
  "error": "unauthorized",
  "fields": null
}
```

Forbidden:

```json
{
  "error": "forbidden",
  "fields": null
}
```

Not found:

```json
{
  "error": "not found",
  "fields": null
}
```

## 7. Postman Collection

A Postman collection should be included in the repository under:

- `postman/TaskFlow.postman_collection.json`

Optional environment file:

- `postman/TaskFlow.local.postman_environment.json`

Recommended Postman variables:

- `baseUrl=http://localhost:8080`
- `token=<jwt from login>`

## 8. Environment Variables

Copy `.env.example` to `.env`.

Required values:

- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `JWT_SECRET`

## 9. What I'd Do With More Time

- Add integration tests for auth and task flows
- Add structured request logging
- Add pagination to list endpoints
- Add the optional `/projects/:id/stats` endpoint
- Improve API response consistency further
- Add a stronger local test profile so JVM tests do not depend on manually provided environment variables
