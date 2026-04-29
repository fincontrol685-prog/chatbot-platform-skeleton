# 🔍 Diagnóstico: Dashboard Não Carrega

Se o dashboard não está carregando dados quando você clica nele, siga este guia de diagnóstico.

## ✅ Checklist Rápido

### 1. Verificar se o Backend está rodando
```bash
curl -v http://localhost:8080/actuator/health 2>&1 | grep -i "up\|down\|connection refused"
```

**Esperado:** `"status":"UP"`
**Problema:** "Connection refused" → Backend não está rodando

### 2. Verificar se você está autenticado
Abra o **DevTools do navegador** (F12):
1. Clique em **Console**
2. Execute:
```javascript
localStorage.getItem('access_token')
```

**Esperado:** Um token JWT começando com `eyJ...`
**Problema:** Retorna `null` → Você não está logado. Faça login primeiro!

### 3. Verificar se o token é válido
No console do navegador, execute:
```javascript
const token = localStorage.getItem('access_token');
if (token) {
  const payload = JSON.parse(atob(token.split('.')[1]));
  console.log('Token expira em:', new Date(payload.exp * 1000));
  console.log('Agora é:', new Date());
}
```

**Problema:** A data de expiração é menor que "Agora" → Token expirou. Faça login novamente!

### 4. Verificar a requisição da API
No DevTools, clique em **Network** e então **atualizar** a página do dashboard:
1. Procure por requisição para `dashboard/stats`
2. Verifique o status HTTP
   - **401** → Você não está autenticado (veja step 2-3)
   - **500** → Erro no servidor (veja logs do backend)
   - **200** → Tudo OK, mas dados não aparecem (problema no frontend)

### 5. Conferir o valor do Token na requisição
Em **Network → dashboard/stats → Headers → autorização**:
1. Verifique se tem `Authorization: Bearer eyJ...`
2. Se não tiver, o interceptor não adicionou o token

## 🔧 Soluções Rápidas

### Solução 1: Fazer Login Novamente
```bash
# Terminal
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Ou pelo frontend: vá para `/login` e faça login novamente.

### Solução 2: Limpar localStorage e fazer login
No console do navegador:
```javascript
localStorage.clear();
location.reload();
// Você será redirecionado para login
```

### Solução 3: Backend não está respondendo?
```bash
# Iniciar backend
cd /home/robertojr/chatbot-platform-skeleton
./mvnw clean install -DskipTests
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar
```

## 📊 Teste Completo (Backend + Frontend)

### 1. Obtenha um token de teste:
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.accessToken')
echo "Token: $TOKEN"
```

### 2. Teste o endpoint com o token:
```bash
curl -v http://localhost:8080/api/analytics/dashboard/stats \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
```

**Esperado:**
```json
{
  "botCount": 1,
  "activeConversationCount": 0,
  "totalMessageCount": 0,
  "userCount": 1
}
```

### 3. Se retorna `{}` vazio
Significa que o banco de dados não tem dados inicializados. Execute:
```bash
# Reiniciar a aplicação - ela criará dados iniciais
pkill -f "chatbot-platform-skeleton"
sleep 2
cd /home/robertojr/chatbot-platform-skeleton
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar
```

## 📋 Logs para Debugar

### Logs do Backend
```bash
# Windows 
tail -f /tmp/backend.log

# Linux
tail -f /tmp/backend.log
```

Procure por:
- `401` → Problema de autenticação
- `403` → Falta de permissão
- `500` → Erro interno
- `SQLException` → Problema no banco de dados

### Logs do Frontend (DevTools)
**Console (F12 → Console)**
- Erros vermelhos indicam problemas
- Procure por mensagens sobre "401" ou "Unauthorized"

## 🆘 Se Nada Funcionar

1. **Reinicie tudo:**
```bash
# Terminal 1: Backend
pkill -f "chatbot-platform-skeleton"
cd /home/robertojr/chatbot-platform-skeleton
./mvnw clean install -DskipTests
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar

# Terminal 2: Frontend  
cd /home/robertojr/chatbot-platform-skeleton/frontend
npm start
```

2. **Limpe cache do navegador:**
   - Abra DevTools (F12)
   - Clique com botão direito no ícone de reload
   - Selecione "Hard refresh" ou "Clear cache"

3. **Verifique MySQL:**
```bash
# Ver se MySQL está rodando
mysql -u root -p -e "SELECT VERSION();"

# Ver se o banco 'chatBox' existe
mysql -u root -p -e "SHOW DATABASES LIKE 'chatBox';"
```

---

## 📞 Mais Informações

- [API Endpoints Completo](./API_ENDPOINTS_COMPLETO.md)
- [Guia de Login](./GUIA_RAPIDO_LOGIN.md)
- [Arquitetura da Plataforma](./architecture.md)

