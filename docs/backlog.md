Backlog inicial (épicos e histórias)

Epic 1: Autenticação e Segurança
- Story 1.1: Implementar login com JWT (AuthController, AuthService) - 5 pontos
- Story 1.2: Implementar refresh tokens e logout - 3 pontos
- Story 1.3: RBAC básica (ADMIN, GESTOR, USUARIO) e testes - 5 pontos

Epic 2: Gestão de Usuários e Perfis
- Story 2.1: CRUD de usuários (Admin) - 5 pontos
- Story 2.2: Atribuição de roles - 3 pontos

Epic 3: Bot Management
- Story 3.1: CRUD de bots - 5 pontos
- Story 3.2: Ativar/Desativar bot - 2 pontos
- Story 3.3: Bot config (JSON) editor UI - 8 pontos

Epic 4: Flow Engine e Intents
- Story 4.1: Modelagem de Flow simples (JSON) CRUD - 8 pontos
- Story 4.2: Executor de fluxo simples (single-step) - 8 pontos
- Story 4.3: CRUD de intents e utterances - 5 pontos

Epic 5: Conversa e Session
- Story 5.1: Endpoint de mensagens e sessão - 5 pontos
- Story 5.2: Persistência de histórico - 3 pontos

Epic 6: Dashboard e Métricas
- Story 6.1: Métricas básicas e endpoint /metrics/overview - 5 pontos
- Story 6.2: Frontend dashboard inicial com gráficos - 8 pontos

Epic 7: Infra & CI
- Story 7.1: Dockerfile backend/frontend e docker-compose - 3 pontos
- Story 7.2: CI pipeline (build + tests + lint) - 5 pontos

Estimada total (MVP): ~70 pontos

Notas

- Priorizar Epics 1, 2 e 3 para um MVP funcional.
- Incluir testes unitários e de integração para endpoints críticos.
