# 📡 GUIA COMPLETO DE ENDPOINTS API REST

## 🔐 Autenticação

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

---

## 🤖 Bot Management (`/api/bots`)

### Listar Bots
```http
GET /api/bots
Authorization: Bearer {TOKEN}

Response 200: [
  {
    "id": 1,
    "name": "Bot Atendimento",
    "key": "bot_atendimento",
    "enabled": true
  }
]
```

### Criar Bot
```http
POST /api/bots
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "name": "Bot Vendas",
  "key": "bot_vendas",
  "config": "{...json...}"
}

Response 201
```

### Ativar/Desativar Bot
```http
PATCH /api/bots/{id}/activate?active=false
Authorization: Bearer {TOKEN}

Response 200
```

---

## 💬 Conversas (`/api/conversations`)

### Criar Conversa
```http
POST /api/conversations
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "botId": 1,
  "userId": 1,
  "title": "Conversa Cliente X"
}

Response 201: {
  "id": 1,
  "botId": 1,
  "userId": 1,
  "title": "Conversa Cliente X",
  "status": "ACTIVE",
  "messageCount": 0,
  "createdAt": "2024-02-17T10:30:00Z"
}
```

### Listar Conversas por Bot
```http
GET /api/conversations/bot/1?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {
  "content": [...],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0,
  "pageSize": 10
}
```

### Listar Conversas Ativas
```http
GET /api/conversations/bot/1/active?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Buscar Conversa
```http
GET /api/conversations/1
Authorization: Bearer {TOKEN}

Response 200: {
  "id": 1,
  "botId": 1,
  "status": "ACTIVE",
  ...
}
```

### Encerrar Conversa
```http
PATCH /api/conversations/1/close
Authorization: Bearer {TOKEN}

Response 200: {
  "status": "CLOSED",
  "closedAt": "2024-02-17T11:00:00Z"
}
```

### Renomear Conversa
```http
PATCH /api/conversations/1/title?title=Nova%20Conversa
Authorization: Bearer {TOKEN}

Response 200
```

### Contar Conversas Ativas
```http
GET /api/conversations/bot/1/count
Authorization: Bearer {TOKEN}

Response 200: 25
```

---

## 📨 Mensagens (`/api/messages`)

### Adicionar Mensagem
```http
POST /api/messages/conversation/1
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "messageType": "USER",
  "content": "Olá, qual é o horário de atendimento?"
}

Response 201: {
  "id": 1,
  "conversationId": 1,
  "senderId": 1,
  "senderUsername": "cliente@example.com",
  "messageType": "USER",
  "content": "Olá, qual é o horário de atendimento?",
  "createdAt": "2024-02-17T10:35:00Z"
}
```

### Listar Mensagens (Paginado)
```http
GET /api/messages/conversation/1?page=0&size=20
Authorization: Bearer {TOKEN}

Response 200: {
  "content": [
    {
      "id": 1,
      "messageType": "USER",
      "content": "Olá",
      "createdAt": "2024-02-17T10:35:00Z"
    },
    {
      "id": 2,
      "messageType": "BOT",
      "content": "Oi! Como posso ajudar?",
      "createdAt": "2024-02-17T10:35:05Z"
    }
  ],
  "totalElements": 10
}
```

### Obter Histórico Completo
```http
GET /api/messages/conversation/1/history
Authorization: Bearer {TOKEN}

Response 200: [
  {"id": 1, "messageType": "USER", "content": "Olá"},
  {"id": 2, "messageType": "BOT", "content": "Oi!"},
  ...
]
```

### Marcar Mensagem para Revisão
```http
PATCH /api/messages/1/flag
Authorization: Bearer {TOKEN}

Response 200: {
  "isFlagged": true
}
```

### Listar Mensagens Marcadas
```http
GET /api/messages/bot/1/flagged?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Tempo Médio de Resposta
```http
GET /api/messages/bot/1/stats/avg-response-time
Authorization: Bearer {TOKEN}

Response 200: 2500 (em milissegundos)
```

### Score de Sentimento Médio
```http
GET /api/messages/bot/1/stats/avg-sentiment
Authorization: Bearer {TOKEN}

Response 200: 0.85 (0.0 = negativo, 1.0 = positivo)
```

---

## 📊 Analytics (`/api/analytics`)

### Dashboard Stats (KPIs)
```http
GET /api/analytics/dashboard/stats
Authorization: Bearer {TOKEN}

Response 200: {
  "botCount": 5,
  "activeConversationCount": 42,
  "totalMessageCount": 1250,
  "userCount": 150
}
```

### Analytics do Bot
```http
GET /api/analytics/bots/1
Authorization: Bearer {TOKEN}

Response 200: {
  "totalConversations": 100,
  "activeConversations": 25,
  "averageResponseTime": 2500,
  "averageSentimentScore": 0.82
}
```

### Análise de Sentimento
```http
GET /api/analytics/bots/1/sentiment
Authorization: Bearer {TOKEN}

Response 200: {
  "averageScore": 0.82,
  "scale": "0.0 (negativo) a 1.0 (positivo)"
}
```

### Métricas de Conversa
```http
GET /api/analytics/bots/1/metrics
Authorization: Bearer {TOKEN}

Response 200: {
  "totalConversations": 100,
  "activeConversations": 25,
  "closedConversations": 75,
  "closedPercentage": 75.0
}
```

---

## 📋 Templates (`/api/templates`)

### Criar Template
```http
POST /api/templates
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "name": "Atendimento ao Cliente",
  "description": "Template para suporte",
  "config": "{...}",
  "category": "customer-support",
  "iconUrl": "https://...",
  "isPublic": true
}

Response 201: {...}
```

### Listar Templates Públicos
```http
GET /api/templates/public?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Templates por Categoria
```http
GET /api/templates/category/customer-support?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Templates Mais Usados
```http
GET /api/templates/most-used?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Templates Melhor Avaliados
```http
GET /api/templates/top-rated?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Avaliar Template
```http
PATCH /api/templates/1/rate?rating=4.5
Authorization: Bearer {TOKEN}

Response 200: {
  "rating": 4.5
}
```

### Meus Templates
```http
GET /api/templates/user/1?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

---

## 🔔 Notificações (`/api/notifications`)

### Listar Notificações
```http
GET /api/notifications/user/1?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {
  "content": [
    {
      "id": 1,
      "type": "NEW_MESSAGE",
      "title": "Nova mensagem em conversa",
      "isRead": false,
      "createdAt": "2024-02-17T10:30:00Z"
    }
  ]
}
```

### Notificações Não Lidas
```http
GET /api/notifications/user/1/unread?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Contar Não Lidas
```http
GET /api/notifications/user/1/unread-count
Authorization: Bearer {TOKEN}

Response 200: 5
```

### Lista Rápida de Não Lidas
```http
GET /api/notifications/user/1/unread-list
Authorization: Bearer {TOKEN}

Response 200: [
  {
    "id": 1,
    "type": "NEW_MESSAGE",
    "title": "Nova mensagem",
    "isRead": false
  }
]
```

### Marcar como Lida
```http
PATCH /api/notifications/1/mark-as-read
Authorization: Bearer {TOKEN}

Response 200: {
  "isRead": true,
  "readAt": "2024-02-17T11:00:00Z"
}
```

### Marcar Todas como Lidas
```http
PATCH /api/notifications/user/1/mark-all-as-read
Authorization: Bearer {TOKEN}

Response 204
```

### Filtrar por Tipo
```http
GET /api/notifications/user/1/type/NEW_MESSAGE?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

---

## 🔍 Auditoria (`/api/audit-logs`)

### Logs do Usuário
```http
GET /api/audit-logs/user/1?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {
  "content": [
    {
      "id": 1,
      "username": "admin",
      "action": "CREATE",
      "entityType": "CONVERSATION",
      "entityId": 1,
      "status": "SUCCESS",
      "ipAddress": "192.168.1.1",
      "createdAt": "2024-02-17T10:30:00Z"
    }
  ]
}
```

### Logs por Tipo de Entidade
```http
GET /api/audit-logs/entity-type/CONVERSATION?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Logs por ID da Entidade
```http
GET /api/audit-logs/entity/1?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Logs de Falhas
```http
GET /api/audit-logs/failed?page=0&size=10
Authorization: Bearer {TOKEN}

Response 200: {...}
```

### Histórico Completo de Mudanças
```http
GET /api/audit-logs/trail/CONVERSATION/1
Authorization: Bearer {TOKEN}

Response 200: [
  {
    "id": 1,
    "action": "CREATE",
    "username": "admin",
    "oldValue": null,
    "newValue": "{\"id\":1,\"title\":\"Conversa\"}",
    "createdAt": "2024-02-17T10:30:00Z"
  },
  {
    "id": 2,
    "action": "UPDATE",
    "username": "moderator",
    "oldValue": "{\"status\":\"ACTIVE\"}",
    "newValue": "{\"status\":\"CLOSED\"}",
    "createdAt": "2024-02-17T11:00:00Z"
  }
]
```

---

## 🎯 Casos de Uso Implementados

### 1. Cliente Inicia Conversa
```
POST /api/conversations
POST /api/messages/conversation/{id}
GET /api/messages/conversation/{id}/history
```

### 2. Bot Responde
```
POST /api/messages/conversation/{id} (messageType: BOT)
GET /api/analytics/bots/{botId}/stats/avg-response-time
```

### 3. Moderador Revisa
```
GET /api/conversations/bot/{botId}/active
GET /api/messages/{id}
PATCH /api/messages/{id}/flag
GET /api/audit-logs/trail/CONVERSATION/{id}
```

### 4. Manager Analisa
```
GET /api/analytics/dashboard/stats
GET /api/analytics/bots/{botId}
GET /api/analytics/bots/{botId}/sentiment
GET /api/analytics/bots/{botId}/metrics
```

### 5. Admin Monitora
```
GET /api/audit-logs/user/{userId}
GET /api/audit-logs/failed
PATCH /api/bots/{id}/activate
```

---

## 📊 Resumo de Endpoints

| Método | Rota | Descrição | Autenticação |
|--------|------|-----------|--------------|
| GET | `/api/bots` | Listar bots | ✅ |
| POST | `/api/bots` | Criar bot | ✅ ADMIN/GESTOR |
| PATCH | `/api/bots/{id}/activate` | Ativar/desativar | ✅ ADMIN/GESTOR |
| POST | `/api/conversations` | Criar conversa | ✅ |
| GET | `/api/conversations/{id}` | Buscar conversa | ✅ |
| GET | `/api/conversations/bot/{id}` | Listar por bot | ✅ |
| GET | `/api/conversations/bot/{id}/active` | Ativas | ✅ |
| PATCH | `/api/conversations/{id}/close` | Encerrar | ✅ ADMIN/GESTOR |
| PATCH | `/api/conversations/{id}/title` | Renomear | ✅ ADMIN/GESTOR |
| POST | `/api/messages/conversation/{id}` | Adicionar msg | ✅ |
| GET | `/api/messages/conversation/{id}` | Listar msg | ✅ |
| GET | `/api/messages/conversation/{id}/history` | Histórico | ✅ |
| PATCH | `/api/messages/{id}/flag` | Marcar | ✅ ADMIN/GESTOR |
| GET | `/api/analytics/dashboard/stats` | KPIs | ✅ |
| GET | `/api/analytics/bots/{id}` | Stats bot | ✅ |
| POST | `/api/templates` | Criar template | ✅ ADMIN/GESTOR |
| GET | `/api/templates/public` | Templates públicos | ✅ |
| PATCH | `/api/templates/{id}/rate` | Avaliar | ✅ |
| GET | `/api/notifications/user/{id}` | Notificações | ✅ |
| PATCH | `/api/notifications/{id}/mark-as-read` | Marcar lido | ✅ |
| GET | `/api/audit-logs/user/{id}` | Logs usuário | ✅ ADMIN/GESTOR |
| GET | `/api/audit-logs/trail/{type}/{id}` | Histórico | ✅ ADMIN/GESTOR |

---

## ✅ Total: 40+ Endpoints Profissionais Implementados

