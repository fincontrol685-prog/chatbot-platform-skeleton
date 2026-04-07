# 🎓 GUIA RÁPIDO - USANDO AS NOVAS FUNCIONALIDADES

**Data:** 2026-04-07  
**Versão:** 1.0 - Completo

---

## 🏢 1. GERENCIAMENTO PROFISSIONAL

### Criar Departamento
```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tecnologia",
    "code": "TEC",
    "description": "Departamento de Tecnologia",
    "location": "São Paulo"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "name": "Tecnologia",
  "code": "TEC",
  "description": "Departamento de Tecnologia",
  "location": "São Paulo",
  "isActive": true,
  "createdAt": "2026-04-07T12:00:00-03:00"
}
```

### Criar Equipe
```bash
curl -X POST http://localhost:8080/api/teams \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Team",
    "code": "BACKEND",
    "description": "Equipe de desenvolvimento backend",
    "departmentId": 1,
    "maxConversationsPerUser": 15
  }'
```

### Adicionar Membro à Equipe
```bash
curl -X POST http://localhost:8080/api/teams/1/members/5 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Designar Líder de Equipe
```bash
curl -X PUT http://localhost:8080/api/teams/1/lead/5 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Listar Equipes de um Departamento
```bash
curl -X GET "http://localhost:8080/api/teams/department/1?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🔐 2. SEGURANÇA 2FA (Two-Factor Authentication)

### Iniciar Setup de 2FA
```bash
curl -X POST http://localhost:8080/api/security/2fa/init \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Resposta:**
```json
{
  "id": 1,
  "userId": 123,
  "isEnabled": false,
  "method": "TOTP",
  "isVerified": false,
  "qrCode": "otpauth://totp/user@email.com?secret=XXXXX&issuer=ChatbotPlatform"
}
```

**O quê fazer com o QR Code:**
1. Escanear com Google Authenticator, Authy, ou Microsoft Authenticator
2. Receber códigos de 6 dígitos que mudam a cada 30 segundos
3. Usar para verificar

### Verificar Código TOTP e Ativar 2FA
```bash
curl -X POST http://localhost:8080/api/security/2fa/verify \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"code": "123456"}'
```

**Resposta (inclui 10 Backup Codes):**
```json
{
  "id": 1,
  "userId": 123,
  "isEnabled": true,
  "isVerified": true,
  "method": "TOTP",
  "createdAt": "2026-04-07T12:00:00-03:00",
  "lastVerifiedAt": "2026-04-07T12:05:00-03:00"
}
```

### Validar Código TOTP (para login)
```bash
curl -X POST http://localhost:8080/api/security/2fa/validate \
  -H "Content-Type: application/json" \
  -d '{"userId": 123, "code": "123456"}'
```

**Resposta:**
```json
{
  "isValid": true
}
```

### Verificar Status 2FA
```bash
curl -X GET http://localhost:8080/api/security/2fa/status \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Desativar 2FA
```bash
curl -X DELETE http://localhost:8080/api/security/2fa \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 📋 3. COMPLIANCE GDPR/LGPD

### Registrar Consentimento
```bash
curl -X POST "http://localhost:8080/api/compliance/consent?consentType=DATA_PROCESSING" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Tipos de Consentimento:**
- `DATA_PROCESSING` - Processamento de dados
- `MARKETING` - Marketing e comunicações
- `COOKIES` - Cookies e tracking
- `PROFILING` - Criação de perfis

### Listar Meus Consentimentos
```bash
curl -X GET http://localhost:8080/api/compliance/consent/my \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Resposta:**
```json
[
  {
    "id": 1,
    "userId": 123,
    "consentType": "DATA_PROCESSING",
    "isGranted": true,
    "consentVersion": "1.0",
    "createdAt": "2026-04-07T12:00:00-03:00",
    "withdrawnAt": null
  }
]
```

### Revogar Consentimento (GDPR - Direito de Retirar)
```bash
curl -X DELETE "http://localhost:8080/api/compliance/consent/DATA_PROCESSING" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Solicitar Exclusão de Dados (GDPR - Direito ao Esquecimento)
```bash
curl -X POST "http://localhost:8080/api/compliance/data-deletion/request?reason=Desejo%20deletar%20minha%20conta" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Resposta:**
```json
{
  "id": 1,
  "userId": 123,
  "status": "PENDING",
  "reason": "Desejo deletar minha conta",
  "requestedAt": "2026-04-07T12:00:00-03:00",
  "processedAt": null
}
```

### Listar Requisições de Exclusão Pendentes (ADMIN)
```bash
curl -X GET "http://localhost:8080/api/compliance/data-deletion/pending?page=0&size=10" \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Aprovar Exclusão de Dados (ADMIN)
```bash
curl -X PUT http://localhost:8080/api/compliance/data-deletion/1/approve \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Rejeitar Exclusão de Dados (ADMIN)
```bash
curl -X PUT http://localhost:8080/api/compliance/data-deletion/1/reject \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

## 📊 4. ANALYTICS AVANÇADO

### Registrar Métrica
```bash
curl -X POST http://localhost:8080/api/analytics-advanced/metrics \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "metricType": "CONVERSATION_COUNT",
    "metricValue": 42.5,
    "botId": 1,
    "departmentId": 1,
    "teamId": 1,
    "periodDate": "2026-04-07",
    "periodHour": 10
  }'
```

**Tipos de Métricas Suportadas:**
- `CONVERSATION_COUNT` - Número de conversas
- `MESSAGE_COUNT` - Número de mensagens
- `AVG_RESPONSE_TIME` - Tempo médio de resposta
- `SENTIMENT_SCORE` - Score de sentimento
- `USER_SATISFACTION` - Satisfação do usuário

### Obter Métricas de um Bot
```bash
curl -X GET "http://localhost:8080/api/analytics-advanced/bot/1/metrics?metricType=CONVERSATION_COUNT&startDate=2026-04-01&endDate=2026-04-07" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Obter Métricas de uma Equipe
```bash
curl -X GET "http://localhost:8080/api/analytics-advanced/team/1/metrics?metricType=CONVERSATION_COUNT&startDate=2026-04-01&endDate=2026-04-07" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Obter Métricas de um Departamento
```bash
curl -X GET "http://localhost:8080/api/analytics-advanced/department/1/metrics?metricType=CONVERSATION_COUNT&startDate=2026-04-01&endDate=2026-04-07" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Criar Relatório Customizado
```bash
curl -X POST http://localhost:8080/api/analytics-advanced/reports \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Relatório de Conversas Abril",
    "description": "Análise de conversas do mês de abril",
    "isPublic": false,
    "metricTypes": "CONVERSATION_COUNT,AVG_RESPONSE_TIME",
    "filters": "{\"botId\": 1, \"dateRange\": \"2026-04-01to2026-04-30\"}"
  }'
```

### Listar Meus Relatórios
```bash
curl -X GET "http://localhost:8080/api/analytics-advanced/reports/my?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Obter Um Relatório
```bash
curl -X GET http://localhost:8080/api/analytics-advanced/reports/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Atualizar Relatório
```bash
curl -X PUT http://localhost:8080/api/analytics-advanced/reports/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Relatório Atualizado",
    "isPublic": true
  }'
```

### Exportar Métricas para Excel
```bash
curl -X POST "http://localhost:8080/api/analytics-advanced/export/excel?reportName=MeuRelatorio" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[
    {
      "metricType": "CONVERSATION_COUNT",
      "metricValue": 42.5,
      "botId": 1,
      "periodDate": "2026-04-07"
    }
  ]' \
  --output relatorio.xlsx
```

### Exportar Métricas para CSV
```bash
curl -X POST "http://localhost:8080/api/analytics-advanced/export/csv?reportName=MeuRelatorio" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[...]' \
  --output relatorio.csv
```

---

## 🔄 FLUXO DE TRABALHO EXEMPLO

### Cenário: Novo Usuário com Segurança Completa

**1. Admin cria departamento**
```bash
POST /api/departments
→ ID do departamento: 1
```

**2. Admin cria equipe no departamento**
```bash
POST /api/teams
→ ID da equipe: 1
```

**3. Admin adiciona usuário à equipe**
```bash
POST /api/teams/1/members/123
```

**4. Usuário ativa 2FA**
```bash
POST /api/security/2fa/init
→ Obter QR Code
POST /api/security/2fa/verify?code=123456
→ Receber backup codes
```

**5. Usuário concorda com consentimentos**
```bash
POST /api/compliance/consent?consentType=DATA_PROCESSING
POST /api/compliance/consent?consentType=MARKETING
```

**6. Sistema registra métricas**
```bash
POST /api/analytics-advanced/metrics
(registra automaticamente cada conversa, mensagem, etc)
```

**7. Admin gera relatório de performance**
```bash
POST /api/analytics-advanced/reports (criar)
POST /api/analytics-advanced/export/excel (exportar)
```

---

## 🔐 SEGURANÇA - BOAS PRÁTICAS

### Token JWT
- ✅ Sempre incluir header: `Authorization: Bearer {TOKEN}`
- ✅ Token expira após 24 horas (configurável)
- ✅ Usar HTTPS em produção

### 2FA
- ✅ Backup codes são por uma única vez
- ✅ Se perder acesso, usar backup code
- ✅ Gerar novos codes após usar todos

### GDPR Compliance
- ✅ Registrar consentimento explícito
- ✅ Manter logs de consentimento
- ✅ Respeitar direito ao esquecimento
- ✅ Auditar todas as operações

### Auditoria
- ✅ Todas as operações são registradas
- ✅ Incluindo: usuário, ação, recurso, timestamp
- ✅ Acessível via `/api/audit-logs/`

---

## 📈 MÉTRICAS ÚTEIS PARA TRACKING

### Para Bots
```json
{
  "metricType": "CONVERSATION_COUNT",
  "botId": 1,
  "dimensionKey": "status",
  "dimensionValue": "closed"
}
```

### Para Equipes
```json
{
  "metricType": "AVG_RESPONSE_TIME",
  "teamId": 1,
  "metricValue": 2.5,
  "periodDate": "2026-04-07"
}
```

### Para Departamentos
```json
{
  "metricType": "USER_SATISFACTION",
  "departmentId": 1,
  "metricValue": 4.5
}
```

---

## ❓ PERGUNTAS FREQUENTES

**P: O que fazer se perder os backup codes de 2FA?**  
R: Entrar em contato com o admin para ressetar 2FA. Requer verificação de identidade.

**P: Posso dividir um departamento em subdepartamentos?**  
R: Sim! Use `parentDepartmentId` ao criar um departamento.

**P: Quantos membros uma equipe pode ter?**  
R: Sem limite, mas há limite de conversas por usuário (`maxConversationsPerUser`).

**P: Como exportar dados para GDPR?**  
R: Solicitar ao admin que execute `/api/compliance/consent/my` e exporte resultados.

**P: As métricas são em tempo real?**  
R: São registradas em tempo real, mas agregação pode levar alguns minutos.

---

## 🎯 PRÓXIMAS FUNCIONALIDADES (Em Breve)

- ⏰ Relatórios agendados automáticos
- 🔗 Integrações com sistemas externos
- 🤖 Automação de workflows
- 📱 Notificações push em tempo real
- 🌐 Chat em tempo real com WebSockets

---

**Última Atualização:** 2026-04-07  
**Status:** ✅ Pronto para Produção

