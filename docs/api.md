API REST - Contratos e Endpoints (Visão Geral)

Resumo

Esta especificação descreve os principais endpoints REST para gerenciar autenticação, usuários, bots, fluxos, intents, mensagens e métricas.

Prefixo: /api

Autenticação

POST /api/auth/login
- Descrição: Autentica usuário e retorna access + refresh token
- Body (application/json):
  {
    "username": "string",
    "password": "string"
  }
- Response 200:
  {
    "accessToken": "jwt",
    "refreshToken": "jwt",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
- Erros: 401 Unauthorized

POST /api/auth/refresh
- Descrição: Gera novo access token a partir do refresh token
- Body:
  { "refreshToken": "string" }
- Response 200: { "accessToken": "jwt", "expiresIn": 3600 }

POST /api/auth/logout
- Descrição: Invalida refresh token (opcionalmente blacklist do jti)
- Body: { "refreshToken": "string" }
- Response: 204 No Content

Usuários e Perfis

GET /api/users
- Permissões: ADMIN, GESTOR
- Descrição: Lista usuários (paginação)
- Query params: page, size, sort
- Response: Paginated list de UserDTO

POST /api/users
- Permissões: ADMIN
- Body: UserCreateDTO { username, email, password, roles[] }
- Response: 201 Created com UserDTO

GET /api/roles
- Permissões: ADMIN
- Descrição: Lista roles configuradas

Bots

GET /api/bots
- Permissões: ADMIN, GESTOR, USUARIO (apenas bots públicos/permitidos)
- Response: lista de BotDTO { id, name, key, enabled, ownerId }

POST /api/bots
- Permissões: ADMIN, GESTOR
- Body: BotCreateDTO { name, config }
- Response: 201 Created com BotDTO

GET /api/bots/{botId}
- Permissões: ADMIN, GESTOR, USUARIO (se permitido)
- Response: BotDetailDTO (com flows, intents resumidos)

PATCH /api/bots/{botId}/activate
- Permissões: ADMIN, GESTOR
- Action: ativa/desativa bot

Flows (Fluxos)

GET /api/bots/{botId}/flows
- Permissões: ADMIN, GESTOR
- Response: lista de FlowDTO { id, name, type, enabled }

POST /api/bots/{botId}/flows
- Permissões: ADMIN, GESTOR
- Body: FlowCreateDTO { name, type, definition (JSON) }
- Response: 201 Created

PUT /api/bots/{botId}/flows/{flowId}
- Permissões: ADMIN, GESTOR
- Body: FlowUpdateDTO

Intents e Utterances

GET /api/bots/{botId}/intents
- Permissões: ADMIN, GESTOR

POST /api/bots/{botId}/intents
- Body: IntentCreateDTO { name, confidenceThreshold }

POST /api/intents/{intentId}/utterances
- Body: { text: "string" }

Roteamento de Mensagens (Conversa)

POST /api/bots/{botId}/messages
- Permissões: USUARIO (autenticado)
- Body: { sessionId (opcional), message: "texto" }
- Response: { sessionId, messages: [ { sender, text, metadata } ] }
- Observações: Endpoint principal consumido pelo frontend. Internamente orquestra Flow Engine e NLP.

Sessões e Histórico

GET /api/sessions/{sessionId}
- Permissões: ADMIN, GESTOR, dono da sessão
- Response: histórico de mensagens e estado atual

Métricas e Dashboard

GET /api/metrics/overview
- Permissões: ADMIN, GESTOR
- Response: { activeChats, totalInteractions, uniqueUsers, botsActive }

GET /api/metrics/bot/{botId}
- Permissões: ADMIN, GESTOR
- Response: séries temporais e KPIs do bot

Erros e Formato de Resposta

Padrão de erro (application/json):
{
  "timestamp": "2026-01-29T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Descrição do erro",
  "path": "/api/.."
}

Notas de Implementação

- Use DTOs para entrada/saída e MapStruct para mapeamento entre entidades e DTOs.
- Documentar APIs com OpenAPI (Springdoc OpenAPI) para gerar a UI e o contrato.
- Defina roles requeridas em cada endpoint com @PreAuthorize ou via configuration de HttpSecurity.
