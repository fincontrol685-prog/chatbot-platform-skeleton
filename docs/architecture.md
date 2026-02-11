Arquitetura da Plataforma de Chatbot Corporativa

Visão Geral

Esta documentação descreve uma arquitetura em camadas para uma plataforma de chatbot personalizável, com foco em ambientes corporativos.

Principais Tecnologias

- Backend: Java 17+, Spring Boot, Spring Security (JWT), JPA/Hibernate
- Banco de Dados: Oracle (12c/19c/21c recomendado)
- Frontend: Angular (versão recente) + Angular Material
- Infra: Docker, CI/CD (GitHub Actions / GitLab CI / Jenkins), Observabilidade (OpenTelemetry, Prometheus, Grafana)

Camadas e Componentes

1. API Layer (REST)
   - Controllers REST expõem APIs para o frontend e integrações externas.
   - Responsável por autenticação, autorização, validação de entrada e tradução de exceções.

2. Security Layer
   - Spring Security com JWT para autenticação/autorizações.
   - Suporte para refresh tokens e, opcionalmente, integração OIDC para SSO corporativo.

3. Service / Business Layer
   - Regras de domínio para bots, fluxos, intents e roteamento de mensagens.
   - Orquestra a execução de fluxos e integrações com NLP/IA.

4. Domain / Persistence Layer
   - Entidades JPA, repositórios (DAO) e mapeamentos com Hibernate.
   - Migrations gerenciadas por Flyway ou Liquibase.

5. Integration / Adapter Layer
   - Adaptadores para APIs externas (canal de mensagens, provedores de IA, sistemas corporativos).
   - Camada para abstrair provedores de NLP ou IA generativa.

6. Infrastructure
   - Configuração de conexão com Oracle, pooling (HikariCP), caching (Redis optional), filas/event-bus (Kafka optional), logging e métricas.

Componentes Funcionais

- Auth Service: login, refresh, logout, gestão de sessões.
- User & RBAC: CRUD de usuários, perfis (ADMIN, GESTOR, USUÁRIO), permissões finas.
- Bot Management: cadastro/ativação/desativação de bots.
- Flow Engine: editor e executor de fluxos de conversa (modelagem visual opcional).
- NLP / Intent Manager: cadastro de intents, utterances e respostas.
- Analytics: coleta de métricas e dashboards (interações, sucesso de intents, latência).
- Integration Gateway: rotas para integrações externas e provedores de IA.

Fluxo de Dados (exemplo: usuário conversando pelo front)

1. Frontend envia mensagem para API REST /api/bots/{botId}/messages com JWT.
2. API valida JWT e resolve identidade/permissões.
3. Service layer recupera estado do fluxo, invoca Flow Engine para determinar próximo passo.
4. Flow Engine pode consultar Intents via NLP local ou delegar a provedor de IA externo.
5. Resposta é persistida (session, history) e eventos relevantes enviados para Analytics/Event bus.
6. Frontend recebe resposta e atualiza UI.

Observabilidade e Operações

- Logging estruturado (JSON logs) e correlação por request-id.
- Tracing distribuído (OpenTelemetry) para seguir execução entre integrações.
- Métricas (Prometheus) para número de interações, latência, taxa de erro.
- Health checks (readiness/liveness) para Kubernetes.

Escalabilidade

- Separar read/write heavy components (DB otimizado, caching para respostas não dinâmicas).
- Estateless application servers (JWT para sessão) e stateful session store opcional (Redis) para contextos longos.
- Horizontally scale services e use message queues para desacoplar picos de carga.

Segurança

- TLS obrigatório entre cliente e API.
- JWT com claims mínimos (sub, roles, jti, iat, exp).
- Proteção CSRF para APIs consumidas por browsers (usar SameSite cookies / CSRF tokens quando apropriado).
- Auditoria de ações críticas (criação/remoção de bots, alteração de fluxos).

Próximos passos

- Gerar modelagem de banco (ER) e scripts de migração iniciais.
- Definir especificação das APIs REST (documento separado).
