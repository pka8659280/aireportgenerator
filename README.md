# AI Reporting Studio

Generate professional reports using AI with XML data and JasperReports templates.

AI Reporting Studio is a Spring Boot web application that lets you describe a report in natural language and have an AI (DeepSeek) generate a fully working JasperReports (JRXML) template for you. You provide XML data, a knowledge base, and prompts; the app uses AI to generate the report layout, compiles it, and exports the result as a PDF — with an automatic AI fix loop when compilation fails.

## Running with Docker (Recommended)

The project ships a [Dockerfile](Dockerfile) and a [docker-compose.yml](docker-compose.yml) that bundle the app **and** its MariaDB database into two containers. On any machine with Docker installed you can run the whole stack without installing JDK, Maven, or MariaDB.

### Prerequisites

- Docker Desktop (or any Docker engine with Compose support)
- A DeepSeek API key

### Step 1 — Create the `.env` file

A **`.env` file is required** — it holds your DeepSeek API key and is read by Docker Compose at startup. Create a file named `.env` in the project root (next to `docker-compose.yml`) with exactly this content:

```
DEEPSEEK_API_KEY=sk-xxxxxxxxxxxxxxxx
```

> Replace `sk-xxxxxxxxxxxxxxxx` with your real DeepSeek API key. The `.env` file is git-ignored and never sent into the Docker image — the key is only injected into the running container at runtime.

### Step 2 — Build and start

```powershell
docker compose up --build -d
```

Then open [http://localhost:8080](http://localhost:8080).

The first run pulls the `mariadb:11.4` image, builds the Spring Boot image (two-stage: Maven build → slim JRE runtime), and waits for MariaDB to become healthy before starting the app. Startup takes roughly 30–60 seconds while the database initializes and seeds the sample data.

### Useful commands

```powershell
# Start containers (after the first build)
docker compose up -d

# View live logs
docker compose logs -f app

# Stop containers (database data is kept in a volume)
docker compose down

# Stop and delete ALL data (fresh start)
docker compose down -v
```

### Rebuilding the Docker image (after code changes)

Whenever you edit Java sources, `pom.xml`, templates, or `application.properties`, rebuild the app image and restart the app container. Build and start are two separate commands:

```powershell
# 1. Build the app image (uses Docker layer caching, so only changed code is recompiled)
docker compose build app

# 2. Recreate the app container with the new image (database volume is preserved)
docker compose up -d app
```

The MariaDB database volume is preserved across rebuilds, so your data stays intact.

After rebuilding, verify the app booted cleanly:

```powershell
docker compose ps
docker compose logs -f app --tail 50
```

### Running on another device

Copy the project folder (or clone it from git) to the target machine and run the same quick-start commands. No local Java, Maven, or MariaDB installation is needed — only Docker. Ports `8080` (app) and `3306` (database) must be free; change the `ports:` mappings in [docker-compose.yml](docker-compose.yml) if needed.

> **Security:** the DeepSeek API key is never stored in the image. It is read at runtime from the `DEEPSEEK_API_KEY` environment variable, which docker compose loads from the `.env` file (e.g. `DEEPSEEK_API_KEY=sk-xxxx`). Share the image freely — the recipient still needs their own key.

## Running Locally (without Docker)

Prefer running against a local MariaDB and JDK instead? Here's the manual setup.

### Prerequisites

- JDK 17+
- MariaDB running locally (default database `ai_reporting`)
- A DeepSeek API key

### Setup

1. **Create the database and user** (or match `application.properties`):

   ```sql
   CREATE DATABASE ai_reporting;
   CREATE USER 'ai_reporting'@'localhost' IDENTIFIED BY 'ai_reporting123';
   GRANT ALL PRIVILEGES ON ai_reporting.* TO 'ai_reporting'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. **Provide the DeepSeek API key** at runtime via the `DEEPSEEK_API_KEY` environment variable — it is never committed and never baked into the Docker image. Either set the env var:

   ```powershell
   $env:DEEPSEEK_API_KEY = "sk-xxxxxxxxxxxxxxxx"
   ./mvnw.cmd spring-boot:run
   ```

   or edit the git-ignored [application.properties](src/main/resources/application.properties) (`DEEPSEEK_API_KEY=sk-xxxxxxxxxxxxxxxx`).

   > On startup the app seeds sample XML data, knowledge bases, system prompts, and user prompts automatically.

### Running the App

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

## Features

- **AI-powered report generation** — Describe your report in a user prompt; the DeepSeek API generates a JasperReports JRXML template tailored to your XML data and knowledge base.
- **One-click PDF export** — The generated JRXML is compiled and rendered to a downloadable PDF.
- **AI self-healing** — When the generated JRXML fails to compile, the AI analyzes the error and fixes the template automatically (configurable max fix attempts).
- **AI prompt polish** — Enrich a plain-language user prompt into the structured report format, or run a grammar-only polish on the system prompt, with undo/redo support.
- **Stop / cancel** — In-flight report generation can be stopped from the UI, aborting the DeepSeek request.
- **Live console streaming** — Backend generation logs stream to the browser in real time over WebSocket.
- **Content management** — CRUD pages for XML Data, Knowledge bases, System Prompts, and User Prompts, all persisted in MariaDB.
- **AI chat** — A chat page backed by the same DeepSeek model.

## Tech Stack

| Layer       | Technology                                              |
|-------------|---------------------------------------------------------|
| Language    | Java 17                                                 |
| Framework   | Spring Boot 4.0.1 (Web, Data JPA, WebSocket, Thymeleaf) |
| Reporting   | JasperReports 7.0.3 (PDF, Excel, charts, barcode, etc.) |
| AI          | DeepSeek API (OpenAI-compatible chat completions)       |
| Database    | MariaDB (Hibernate/JPA)                                |
| Frontend    | Thymeleaf + Bootstrap 5                                 |
| Build       | Maven (with Maven Wrapper)                              |

## Pages

| URL                          | Purpose                                             |
|------------------------------|-----------------------------------------------------|
| `/`                          | Home page                                           |
| `/ai-reporting-studio`       | Main report generation studio (`/aireport` alias)   |
| `/xmldata`                   | Manage XML data payloads                           |
| `/knowledge`                 | Manage knowledge bases / templates                 |
| `/systemprompt`              | Manage system prompts                              |
| `/userprompt`                | Manage user prompts                                |
| `/chat`                      | AI chat                                            |

## How Report Generation Works

1. The frontend sends XML data, knowledge, system prompt, and user prompt to `POST /api/ai-reporting-studio/generate`.
2. [JRXMLGenerationService](src/main/java/com/demo/aireportstudio/services/JRXMLGenerationService.java) asks DeepSeek to produce a JRXML template.
3. [JasperReportsService](src/main/java/com/demo/aireportstudio/services/JasperReportsService.java) compiles the JRXML; on failure, [JasperReportsAIFixService](src/main/java/com/demo/aireportstudio/services/JasperReportsAIFixService.java) loops with the AI until it compiles (bounded by `app.jrxml.max-fix-attempts`).
4. The report is rendered to PDF and returned as a download.

## Configuration Reference

Key tunables in [application.properties](src/main/resources/application.properties):

- `deepseek.max-retries` / `deepseek.retry-delay-ms` / `deepseek.retry-max-delay-ms` — DeepSeek retry behavior.
- `deepseek.request-timeout-seconds` / `deepseek.generate-deadline-ms` — request timeout and overall generation deadline.
- `app.jrxml.max-fix-attempts` — max AI fix attempts for a failing JRXML.
- `app.jrxml.max-same-error-attempts` — give up when the same compile error repeats consecutively.

## Project Structure

```
src/main/java/com/demo/aireportstudio/
├── controller/   # REST + screen controllers
├── model/        # JPA entities (XmlData, Knowledge, SystemPrompt, UserPrompt)
├── repository/   # Spring Data repositories
├── services/     # Business logic (AI, JasperReports, WebSocket, storage, etc.)
└── config/       # WebSocket configuration
src/main/resources/
├── templates/    # Thymeleaf HTML pages
└── db/           # Sample seed data (SQL)
```

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
