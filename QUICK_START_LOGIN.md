# 🚀 GUIA RÁPIDO - CHATBOT PLATFORM SKELETON

## ✅ Status da Aplicação

✓ **Backend (Spring Boot)** - Compilado e testado  
✓ **Frontend (Angular)** - Compilado com sucesso  
✓ **Usuários padrão** - Criados automaticamente  
✓ **Banco de dados** - H2 em memória (desenvolvimento)  

---

## 👤 Usuários Padrão Criados

Dois usuários foram criados automaticamente no startup:

### 1. **Usuário Admin** (Administrador)
- **Username:** `admin`
- **Senha:** `admin123`
- **Nível:** ADMIN (total acesso)

### 2. **Usuário Teste** (Padrão)
- **Username:** `user`
- **Senha:** `user123`
- **Nível:** USUARIO (acesso limitado)

---

## 🎯 Como Executar a Aplicação

### Opção 1: Executar Backend (Terminal 1)
```bash
cd /home/robertojr/chatbot-platform-skeleton
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar
```

**Esperado:**
- Servidor iniciará na porta **8080**
- Verá mensagens:
  - ✓ Default admin user created
  - ✓ Default test user created
  - ✓ Application initialized successfully!

### Opção 2: Executar Frontend (Terminal 2)
```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend
npm start
```

**Esperado:**
- Angular dev server iniciará na porta **4200**
- Navegador abrirá automaticamente em `http://localhost:4200`
- Será redirecionado para `/login`

---

## 🔐 Primeiro Login - Passo a Passo

1. **Aguarde a página carregar**
   - Você verá a tela de login com campos "Username" e "Password"

2. **Digite as credenciais:**
   - Username: `admin`
   - Password: `admin123`

3. **Clique no botão "Login"**

4. **Sucesso!** 
   - Você será redirecionado para `/bots`
   - Aparecerá a barra de navegação superior com:
     - Link "Chatbot Platform" (home)
     - Link "Bots"
     - Link "Create Bot"
     - Botão "Logout"

---

## 📋 O que foi Corrigido

### 1. **Criação Automática de Usuários**
- **Problema:** Não havia usuários no banco para fazer login
- **Solução:** Criado `DataInitializer.java` que executa no startup
  - Cria automaticamente usuários admin e user
  - Usa BCrypt para criptografia de senhas
  - Verifica se já existem antes de criar (idempotente)

### 2. **Compilação do Backend**
- ✓ Todas as dependências resolvidas
- ✓ DataInitializer compilado e incluído

### 3. **Compilação do Frontend**
- ✓ Angular 16 compilado com sucesso
- ✓ Proxy configurado para chamar backend em `http://localhost:8080`
- ✓ CORS configurado no Spring Boot

---

## 🔗 API Endpoints Importantes

### Autenticação
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### Logout
```
GET http://localhost:4200/login
(Clique no botão "Logout" na barra superior)
```

---

## 🛠️ Tecnologias Usadas

### Backend
- **Java 17**
- **Spring Boot 3.2.4**
- **Spring Security com JWT**
- **H2 Database**
- **Flyway (migrations)**
- **Hibernate/JPA**

### Frontend
- **Angular 16**
- **Angular Material**
- **TypeScript 5.1**
- **RxJS 7.8**

---

## 📁 Estrutura Principal

```
chatbot-platform-skeleton/
├── src/main/java/com/br/chatbotplatformskeleton/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── CorsConfig.java
│   │   └── DataInitializer.java (NOVO!)
│   ├── controller/
│   │   └── AuthController.java
│   ├── service/
│   │   └── AuthService.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   ├── JwtFilter.java
│   │   └── CustomUserDetailsService.java
│   └── domain/
│       ├── UserAccount.java
│       └── Role.java
│
├── frontend/
│   ├── src/app/
│   │   ├── features/
│   │   │   └── auth/login/
│   │   │       ├── login.component.ts
│   │   │       └── login.component.html
│   │   ├── core/
│   │   │   ├── auth.service.ts
│   │   │   ├── auth.guard.ts
│   │   │   └── auth.interceptor.ts
│   │   └── app.module.ts
│   └── proxy.conf.json
```

---

## ✅ Checklist de Verificação

- [x] Backend compila sem erros
- [x] DataInitializer criado
- [x] Usuários padrão criados
- [x] Frontend compila sem erros
- [x] Login testado com sucesso
- [x] CORS configurado
- [x] JWT funcionando
- [x] AuthGuard protegendo rotas

---

## 🐛 Troubleshooting

### Porta 8080 já em uso?
```bash
# Encontrar processo
lsof -i :8080

# Matar processo (PID = número)
kill -9 <PID>
```

### Porta 4200 já em uso?
```bash
# Executar em outra porta
cd frontend && npm start -- --port 4201
```

### CORS error no navegador?
- Verifique se o backend está rodando em `http://localhost:8080`
- Verifique `application.properties`: `cors.allowed-origins=http://localhost:4200`

### Token expirado?
- Tokens JWT expiram em 15 minutos (900 segundos)
- Faça logout e login novamente

---

## 🎓 Próximos Passos Sugeridos

1. **Criar mais usuários** via API ou console H2
2. **Implementar módulo de Bots** (features/bots)
3. **Criar Dashboard** (features/dashboard)
4. **Adicionar testes unitários**
5. **Configurar banco Oracle para produção**

---

## 📞 Suporte

Se encontrar problemas:
1. Verifique os logs do backend: `/tmp/backend.log`
2. Verifique a porta: `curl http://localhost:8080/actuator`
3. Teste direto a API: 
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

**Versão:** 1.0  
**Data:** 17/02/2026  
**Status:** ✅ Pronto para Desenvolvimento

