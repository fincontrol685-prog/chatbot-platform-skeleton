# AGENTS.md

## Overview
This document provides essential knowledge for AI coding agents to be productive in the Chatbot Platform Skeleton project. The platform is a modular chatbot system with a Spring Boot backend and Angular frontend, designed for corporate environments.

---

## Architecture

### Big Picture
- **Frontend**: Angular 16 with Angular Material for UI components.
- **Backend**: Spring Boot 3.2.4 with layered architecture (Controller, Service, Repository).
- **Database**: MySQL 8.0+ with Flyway for migrations.
- **Integration**: External NLP/AI providers and corporate systems via adapter layer.
- **Infrastructure**: Dockerized deployment, CI/CD pipelines, observability tools (Prometheus, Grafana).

### Data Flow Example
1. Frontend sends a message to `/api/bots/{botId}/messages` with JWT.
2. Backend validates JWT, retrieves flow state, and invokes the Flow Engine.
3. Flow Engine determines the next step, optionally querying NLP/AI providers.
4. Response is sent back to the frontend.

---

## Developer Workflows

### Build and Run
- **Frontend**:
  - Install dependencies: `npm install`
  - Run dev server: `ng serve --proxy-config proxy.conf.json`
  - Access: [http://localhost:4200](http://localhost:4200)
- **Backend**:
  - Build: `./mvnw clean package`
  - Run: `./mvnw spring-boot:run`
  - Access: [http://localhost:8080](http://localhost:8080)

### Testing
- **Frontend**: `npm test` (Karma/Jasmine)
- **Backend**: `./mvnw test`

### Docker
- Build frontend image: `docker build -t chatbot-frontend:local .`
- Run container: `docker run -p 8080:80 chatbot-frontend:local`

---

## Project-Specific Conventions

### Backend
- **Security**: JWT-based authentication with optional OIDC integration.
- **Persistence**: JPA entities with repositories in `src/main/java/com/br/chatbotplatformskeleton/repository/`.
- **Services**: Business logic in `src/main/java/com/br/chatbotplatformskeleton/service/`.
- **Controllers**: REST endpoints in `src/main/java/com/br/chatbotplatformskeleton/controller/`.

### Frontend
- **Modules**: Feature-based structure (e.g., `features/conversations/`).
- **Environment Config**: API base URL in `src/environments/environment.ts`.
- **Material Design**: Shared `MaterialModule` for Angular Material components.

---

## Integration Points
- **NLP/AI Providers**: Adapter layer for external services.
- **Corporate Systems**: Integration via REST APIs or message queues.
- **CORS**: Configured in backend (`cors.allowed-origins`).

---

## Key Files and Directories
- **Frontend**:
  - `src/app/features/conversations/`: Conversation components and services.
  - `proxy.conf.json`: Proxy configuration for development.
- **Backend**:
  - `src/main/java/com/br/chatbotplatformskeleton/`: Core backend code.
  - `src/main/resources/db/migration/`: Flyway migration scripts.
- **Docs**:
  - `docs/architecture.md`: Detailed architecture overview.
  - `docs/IMPLEMENTACAO_FASE1.md`: Implementation details for Phase 1.

---

## Examples

### Backend Service
```java
@Service
public class ConversationService {
    public Conversation findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Conversation not found"));
    }
}
```

### Frontend Component
```typescript
@Component({
  selector: 'app-conversation-detail',
  templateUrl: './conversation-detail.component.html',
  styleUrls: ['./conversation-detail.component.css']
})
export class ConversationDetailComponent {
  private loadBot(botId: number): void {
    this.botService.get(botId).subscribe(bot => this.bot = bot);
  }
}
```
