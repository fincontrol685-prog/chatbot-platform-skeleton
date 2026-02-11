Estrutura sugerida do projeto (mono-repo ou multi-repo)

Recomendação: iniciar como mono-repo com pastas `backend/` e `frontend/` para simplificar integração e CI.

Raiz do repositório

- backend/ (Spring Boot app)
  - pom.xml
  - src/main/java/com/company/chatbot
    - ChatbotApplication.java
    - config/ (SecurityConfig, JwtConfig, WebConfig)
    - controller/ (AuthController, UserController, BotController, FlowController, MetricsController)
    - service/ (AuthService, UserService, BotService, FlowService, AnalyticsService)
    - domain/ (JPA entities: UserAccount, Role, Bot, Flow, Intent, Session, MessageHistory)
    - repository/ (Spring Data JPA repositories)
    - dto/ (DTOs de entrada/saída)
    - mapper/ (MapStruct mappers)
    - integration/ (clients para IA, canais, provedor de mensagens)
    - security/ (JwtTokenProvider, JwtFilter, AuthenticationEntryPoint)
    - migration/ (Flyway scripts under db/migrations)
  - src/main/resources/application.yml
  - Dockerfile
  - README.md

- frontend/ (Angular app)
  - package.json
  - angular.json
  - src/
    - app/
      - core/ (auth.service.ts, http-interceptor, guards)
      - shared/ (models, ui-components)
      - features/
        - auth/
        - bots/
        - flows/
        - admin/
      - app.module.ts
  - Dockerfile
  - README.md

- docs/ (documentação gerada)
- db/ (migrations)
- .github/ (CI workflows)

Módulos e responsabilidade

- Core Module (backend): autenticação, segurança, config comum, exceções
- Bot Module: CRUD de bots, ativação/desativação
- Flow Engine: modelagem e execução de fluxos, persistência de definições
- NLP/Intent Module: gerenciamento de intents/utterances e integração com motores NLP
- Analytics Module: captura e exposição de métricas

Padrões de pastas e convenções

- Seguir convenção de package por feature (ex: com.company.chatbot.bot.controller)
- DTOs separados por camada e imutáveis (usar builders)
- Services finos, controllers anêmicos
- Usar MapStruct para mapeamento e evitar lógica de negócio em mappers

Scripts úteis

- mvnw - para build do backend
- npm install && npm run build - para frontend
- flyway:clean/migrate - via CI para aplicar migrations

Notas

- Inicialmente, manter o projeto minimal e modular. Adicionar microservices quando houver necessidade clara de escalar/isolamento.
