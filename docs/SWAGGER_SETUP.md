# 🚀 Swagger - Documentação da API

## Como Acessar o Swagger

Após iniciar a aplicação, acesse o Swagger UI em:

```
http://localhost:8080/swagger-ui.html
```

### URLs Importantes:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

## 📋 Tags de Documentação

A API está organizada em 12 grupos principais:

### 1. **Authentication** (`/api/auth`)
- Login de usuário
- Registro de novo usuário
- Recuperação de senha
- Reset de senha
- Renovação de token JWT

### 2. **Users** (`/api/users`)
- Listar usuários
- Criar usuário
- Obter usuário por ID
- Atualizar usuário
- Atualizar status do usuário
- Listar papéis (roles)

### 3. **Departments** (`/api/departments`)
- Criar departamento
- Listar departamentos ativos
- Buscar departamento por ID
- Buscar departamentos
- Listar departamentos raiz
- Listar subdepartamentos
- Atualizar departamento
- Deletar departamento

### 4. **Teams** (`/api/teams`)
- Criar equipe
- Listar equipes
- Buscar equipe por ID
- Listar equipes por departamento
- Buscar equipes
- Atualizar equipe
- Adicionar membro à equipe
- Remover membro da equipe
- Atribuir líder de equipe
- Deletar equipe

### 5. **Bots** (`/api/bots`)
- Listar todos os bots
- Criar novo bot
- Obter bot por ID
- Atualizar bot
- Ativar/Desativar bot

### 6. **Templates** (`/api/templates`)
- Criar template de bot
- Listar templates públicos
- Buscar template por ID
- Listar templates por categoria
- Listar templates por usuário
- Listar templates mais usados
- Listar templates melhor avaliados
- Atualizar template
- Avaliar template

### 7. **Conversations** (`/api/conversations`)
- Criar conversa
- Obter conversa por ID
- Listar conversas por bot
- Listar conversas por usuário
- Listar conversas ativas
- Fechar conversa
- Atualizar título da conversa
- Contar conversas ativas

### 8. **Messages** (`/api/messages`)
- Adicionar mensagem
- Troca de mensagens (enviar e receber resposta)
- Obter mensagem por ID
- Listar mensagens da conversa
- Obter histórico da conversa
- Marcar mensagem com flag
- Listar mensagens flagged
- Estatísticas das mensagens

### 9. **Analytics** (`/api/analytics`)
- Estatísticas do dashboard
- Análise de bot específico
- Análise de sentimento
- Métricas de conversa

### 10. **Advanced Analytics** (`/api/analytics-advanced`)
- Registrar métrica
- Métricas do bot em período
- Métricas da equipe em período
- Métricas do departamento em período
- Criar relatório customizado
- Listar relatórios acessíveis
- Listar meus relatórios
- Atualizar relatório
- Deletar relatório
- Exportar para Excel
- Exportar para CSV

### 11. **Compliance** (`/api/compliance`)
- Conceder consentimento (GDPR)
- Revogar consentimento
- Meus consentimentos
- Solicitar exclusão de dados (Direito ao Esquecimento)
- Minhas requisições de exclusão
- Aprovar exclusão de dados
- Rejeitar exclusão de dados
- Requisições pendentes de exclusão

### 12. **Notifications** (`/api/notifications`)
- Listar notificações do usuário
- Listar notificações não lidas
- Obter lista de não lidas
- Contar não lidas
- Obter notificação por ID
- Marcar como lida
- Marcar todas como lidas
- Listar notificações por tipo
- Deletar notificação

### 13. **Two Factor Authentication** (`/api/security/2fa`)
- Inicializar 2FA
- Verificar e ativar 2FA
- Validar código 2FA
- Obter status do 2FA
- Desativar 2FA

### 14. **Audit Logs** (`/api/audit-logs`)
- Listar registros de auditoria
- Auditoria por usuário
- Auditoria por tipo de entidade
- Auditoria por ID da entidade
- Operações falhadas
- Trilha de auditoria

## 🔐 Autenticação

Todos os endpoints (exceto `/api/auth/login` e `/api/auth/register`) requerem autenticação via **Bearer Token JWT**.

### Passos para testar com autenticação:

1. Faça login em `/api/auth/login` com suas credenciais
2. Copie o token JWT recebido na resposta
3. Clique no botão **"Authorize"** no Swagger UI
4. Cole o token no campo com o prefixo `Bearer` (ex: `Bearer seu_token_aqui`)
5. Todos os endpoints agora estarão autenticados

## 🔑 Autorização por Papéis

A API utiliza controle de acesso baseado em papéis:

- **ADMIN**: Acesso total à plataforma
- **GESTOR**: Acesso a gerenciamento de equipes, departamentos e analytics
- **USUARIO**: Acesso básico a conversas e bots

## 📊 Testando a API

### 1. Via Swagger UI

1. Acesse http://localhost:8080/swagger-ui.html
2. Selecione um endpoint
3. Clique em "Try it out"
4. Preencha os parâmetros necessários
5. Clique em "Execute"

### 2. Via cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "seu_usuario",
    "password": "sua_senha"
  }'

# Usar o token para acessar endpoints protegidos
curl -X GET http://localhost:8080/api/bots \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 3. Via Postman

1. Importe a URL: http://localhost:8080/api-docs
2. Configure authorization: Bearer Token
3. Teste os endpoints

## 🛠️ Configuração

As configurações do Swagger estão no `application.properties`:

```properties
# Springdoc OpenAPI (Swagger UI)
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.show-actuator=false
springdoc.use-fqn=true
```

## 📖 Recursos Adicionais

- **Documentação Spring Boot**: https://spring.io/projects/spring-boot
- **Springdoc OpenAPI**: https://springdoc.org/
- **OpenAPI 3.0**: https://spec.openapis.org/oas/v3.0.3
- **Swagger UI**: https://swagger.io/tools/swagger-ui/

## ✅ Checklist de Verificação

- [x] Dependência Springdoc OpenAPI adicionada ao pom.xml
- [x] Configuração SwaggerConfig.java criada
- [x] Todos os controllers documentados com @Tag
- [x] Todos os endpoints documentados com @Operation
- [x] Respostas HTTP documentadas com @ApiResponse
- [x] Segurança JWT configurada com @SecurityRequirement
- [x] Propriedades do Swagger configuradas no application.properties
- [x] Projeto compilado com sucesso

## 🎯 Próximos Passos

1. Inicie a aplicação Spring Boot
2. Acesse http://localhost:8080/swagger-ui.html
3. Teste os endpoints através da interface do Swagger
4. Utilize o token JWT para acessar endpoints protegidos
5. Explore a documentação interativa para cada endpoint

---

**Data de Configuração**: 29 de Abril de 2026  
**Versão**: 1.0.0

