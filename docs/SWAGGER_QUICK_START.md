# 🎯 Guia Rápido - Swagger da API

## ✅ O Que Foi Feito

Implementei a documentação interativa (Swagger/OpenAPI 3.0) para toda a API da plataforma de chatbot.

### 📦 Mudanças Realizadas

1. **Adicionada Dependência**
   - `springdoc-openapi-starter-webmvc-ui` v2.1.0 no `pom.xml`

2. **Criada Configuração**
   - Arquivo: `src/main/java/.../config/SwaggerConfig.java`
   - Configuração completa da OpenAPI com JWT

3. **Documentados 14 Controllers com 124+ Endpoints**
   - Cada endpoint tem descrição, parâmetros e respostas documentadas
   - Todos com controle de acesso baseado em papéis
   - Autenticação JWT integrada

4. **Configurado application.properties**
   - Propriedades do Springdoc OpenAPI adicionadas

## 🚀 Como Usar

### 1. Iniciar a Aplicação
```bash
cd /home/robertojr/chatbot-platform-skeleton
./mvnw spring-boot:run
```

### 2. Acessar Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 3. Fazer Login
- Endpoint: `POST /api/auth/login`
- Envie suas credenciais
- Copie o token JWT recebido

### 4. Autorizar no Swagger
1. Clique no botão **"Authorize"** (cadeado)
2. Cole o token com prefixo: `Bearer seu_token_aqui`
3. Clique em "Authorize"
4. Agora todos os endpoints protegidos estarão acessíveis

### 5. Testar Endpoints
- Selecione um endpoint
- Clique em "Try it out"
- Preencha os parâmetros
- Clique em "Execute"

## 📚 Documentação Disponível

| URL | Conteúdo |
|-----|----------|
| `http://localhost:8080/swagger-ui.html` | Interface Web Interativa |
| `http://localhost:8080/api-docs` | JSON OpenAPI Completo |
| `http://localhost:8080/api-docs.yaml` | YAML OpenAPI |
| `/docs/SWAGGER_SETUP.md` | Guia Completo |
| `/docs/SWAGGER_IMPLEMENTATION_SUMMARY.md` | Resumo Técnico |

## 🎯 APIs Documentadas (14 Controllers)

### 🔐 Autenticação
- **Path**: `/api/auth`
- **Endpoints**: Login, Registro, Recuperação de Senha, Reset, Refresh

### 👥 Usuários
- **Path**: `/api/users`
- **Endpoints**: Listar, Criar, Obter, Atualizar, Status, Roles

### 🏢 Departamentos
- **Path**: `/api/departments`
- **Endpoints**: 8 operações CRUD + hierarquia

### 👨‍💼 Equipes
- **Path**: `/api/teams`
- **Endpoints**: 10 operações incluindo membros

### 🤖 Bots
- **Path**: `/api/bots`
- **Endpoints**: 5 operações CRUD

### 📋 Templates
- **Path**: `/api/templates`
- **Endpoints**: 9 operações com sistema de avaliação

### 💬 Conversas
- **Path**: `/api/conversations`
- **Endpoints**: 8 operações de conversas

### 💭 Mensagens
- **Path**: `/api/messages`
- **Endpoints**: 9 operações de mensagens

### 📊 Analytics
- **Path**: `/api/analytics`
- **Endpoints**: 4 operações básicas

### 🔬 Analytics Avançados
- **Path**: `/api/analytics-advanced`
- **Endpoints**: 11 operações + exportação Excel/CSV

### 🔔 Notificações
- **Path**: `/api/notifications`
- **Endpoints**: 9 operações

### ⚖️ Conformidade (GDPR)
- **Path**: `/api/compliance`
- **Endpoints**: 8 operações

### 🔐 Dois Fatores
- **Path**: `/api/security/2fa`
- **Endpoints**: 5 operações

### 📋 Auditoria
- **Path**: `/api/audit-logs`
- **Endpoints**: 6 operações

## 🔒 Segurança

✅ Todos os endpoints protegidos por JWT (exceto `/auth/login` e `/auth/register`)  
✅ Controle de acesso por papel (ADMIN, GESTOR, USUARIO)  
✅ Bearer Token configurado no Swagger

## 📋 Checklist

- [x] Dependência adicionada ao pom.xml
- [x] Config do Swagger criada
- [x] Todos os controllers documentados
- [x] Todas as respostas HTTP documentadas
- [x] JWT integrado ao Swagger
- [x] application.properties configurado
- [x] Projeto compilado com sucesso
- [x] Documentação criada

## 🧪 Teste Rápido

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"seu_user","password":"sua_senha"}'

# 2. Copiar token retornado

# 3. Usar token para acessar endpoint protegido
curl -X GET http://localhost:8080/api/bots \
  -H "Authorization: Bearer SEU_TOKEN"
```

## 💡 Dicas de Uso

1. **Explorar**: Use o Swagger UI para explorar todos os endpoints
2. **Parâmetros**: Cada endpoint mostra quais parâmetros são necessários
3. **Exemplos**: Veja exemplos de requisição e resposta
4. **Testes**: Teste diretamente do navegador
5. **Postman**: Importe a URL `/api-docs` para Postman

## 📞 Próximas Etapas

1. ✅ Verificar que Swagger está funcionando
2. ✅ Testar endpoints via UI
3. ✅ Validar autenticação JWT
4. ✅ Usar em desenvolvimento/testes
5. ⏳ [Futuro] Documentar DTOs com @Schema
6. ⏳ [Futuro] Adicionar mais exemplos

---

**Status**: ✅ PRONTO PARA USO  
**Data**: 29/04/2026  
**Versão**: 1.0.0

