# 🚀 Guia Rápido - Rate Limiting (Fase 4)

## Resumo Executivo

O Rate Limiting foi implementado com **Bucket4j** para proteger endpoints críticos contra abuso e DoS attacks. A implementação usa o **Token Bucket Algorithm** com armazenamento em memória.

---

## ⚙️ Configuração Padrão

```properties
# application.properties - Valores Padrão

# Habilitar/Desabilitar
ratelimit.enabled=true

# User-based (endpoints autenticados)
ratelimit.user.requests=10       # 10 requisições
ratelimit.user.interval-minutes=1 # por minuto

# IP-based (endpoints público de login)
ratelimit.ip.requests=5          # 5 requisições
ratelimit.ip.interval-minutes=1  # por minuto

# Analytics endpoints
ratelimit.analytics.requests=30  # 30 requisições
ratelimit.analytics.interval-minutes=1 # por minuto
```

---

## 📋 Endpoints Protegidos

| Método | Endpoint | Limite | Por |
|--------|----------|--------|-----|
| POST | `/api/auth/login` | 5/min | IP |
| POST | `/api/auth/register` | 5/min | IP |
| POST | `/api/auth/forgot-password` | 5/min | IP |
| POST | `/api/messages/conversation/{id}/exchange` | 10/min | Usuário |
| GET/POST | `/api/analytics/*` | 30/min | Usuário |

---

## 🧪 Testar Rate Limiting

### Via cURL - Teste de Login (5 requisições/min por IP)

```bash
# Salvar em test-rate-limit.sh
#!/bin/bash

echo "Testando Rate Limit de Login (5 req/min por IP)..."
for i in {1..7}; do
  echo -e "\n=== Requisição $i ==="
  curl -s -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"123"}' \
    -w "\nStatus: %{http_code}\nTamanho: %{size_download} bytes\n"
done

bash test-rate-limit.sh
```

**Resultado esperado:**
- Requisições 1-5: `200 OK` ✓
- Requisição 6-7: `429 Too Many Requests` ✗

### Via cURL - Teste de Message Exchange (10 requisições/min por usuário)

```bash
# Salvar em test-message-rate-limit.sh
#!/bin/bash

TOKEN="seu_jwt_token_aqui"

echo "Testando Rate Limit de Message Exchange (10 req/min por usuário)..."
for i in {1..12}; do
  echo -e "\n=== Requisição $i ==="
  curl -s -X POST http://localhost:8080/api/messages/conversation/1/exchange \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"content":"mensagem teste","messageType":"USER_MESSAGE"}' \
    -w "\nStatus: %{http_code}\n"
done

bash test-message-rate-limit.sh
```

---

## 🔧 Personalizar Limites

### Exemplo 1: Aumentar limite de login para 10 requisições/min

```properties
# application.properties
ratelimit.ip.requests=10
ratelimit.ip.interval-minutes=1
```

Depois reiniciar a aplicação:
```bash
mvn spring-boot:run
```

### Exemplo 2: Desabilitar rate limiting em desenvolvimento

```properties
# application.properties
ratelimit.enabled=false
```

### Exemplo 3: Aumentar limite de mensagens para 20/min

```properties
# application.properties
ratelimit.user.requests=20
ratelimit.user.interval-minutes=1
```

---

## 📊 Monitoramento

### Ver Logs de Rate Limit

```bash
# Terminal 1: Iniciar aplicação com log de DEBUG
mvn -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG spring-boot:run

# Terminal 2: Enviar requisições
curl -X POST http://localhost:8080/api/auth/login ...
```

**Saída esperada:**
```
[DEBUG] Rate limit check passed for IP: 192.168.1.100 (remaining: 4)
[DEBUG] Rate limit check passed for IP: 192.168.1.100 (remaining: 3)
[DEBUG] Rate limit check passed for IP: 192.168.1.100 (remaining: 2)
[DEBUG] Rate limit check passed for IP: 192.168.1.100 (remaining: 1)
[DEBUG] Rate limit check passed for IP: 192.168.1.100 (remaining: 0)
[WARN] Rate limit exceeded for IP: 192.168.1.100
```

### Headers de Resposta

```bash
curl -i -X POST http://localhost:8080/api/messages/conversation/1/exchange \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"content":"test"}'

# Resposta com sucesso:
# HTTP/1.1 201 Created
# X-RateLimit-Remaining: 9
# ...

# Resposta com limite excedido:
# HTTP/1.1 429 Too Many Requests
# X-RateLimit-Remaining: 0
# X-RateLimit-Reset: 1651413600
# Content-Type: application/json
# {
#   "error": "Rate limit exceeded. Maximum requests per minute exceeded.",
#   "retryAfter": 60
# }
```

---

## 🏭 Executar Testes

### Testes Unitários de RateLimitService

```bash
cd /home/robertojr/chatbot-platform-skeleton

# Executar só testes de RateLimitService
mvn test -Dtest=RateLimitServiceTest

# Resultado esperado:
# Tests run: 9, Failures: 0, Errors: 0
```

### Testes de Filter

```bash
# Executar testes do filter
mvn test -Dtest=RateLimitingFilterTest

# Resultado esperado:
# Tests run: 6, Failures: 0, Errors: 0
```

### Executar Todos os Testes

```bash
# Executar toda suite de testes
mvn test

# Resultado esperado:
# Tests run: XXX, Failures: 0, Errors: 0
```

---

## 🛠️ Adicionar Rate Limit a Novo Endpoint

### Passo 1: Identificar o endpoint

Exemplo: `/api/novo-endpoint`

### Passo 2: Editar RateLimitingFilter.java

```java
private boolean enforceRateLimit(HttpServletRequest request, HttpServletResponse response, String requestPath) 
        throws IOException {

    // ... endpoints existentes ...

    // Adicionar novo endpoint com rate limit por IP
    if (requestPath.contains("/novo-endpoint")) {
        return enforceIpRateLimit(request, response);
    }

    // Ou com rate limit por usuário
    if (requestPath.contains("/novo-endpoint")) {
        return enforceUserRateLimit(request, response, false, false);
    }

    return true;
}
```

### Passo 3: Testar

```bash
mvn clean compile

# Testar o novo endpoint
curl -X GET http://localhost:8080/api/novo-endpoint ...
```

---

## ⚠️ Troubleshooting

### Problema: Recebendo 429 muito rápido

**Solução 1**: Aumentar o limite
```properties
ratelimit.user.requests=20
```

**Solução 2**: Aumentar intervalo
```properties
ratelimit.user.interval-minutes=2  # 2 minutos
```

**Solução 3**: Desabilitar para debug
```properties
ratelimit.enabled=false
```

### Problema: Rate limit não funciona

**Verificação 1**: Confirmar que está habilitado
```properties
ratelimit.enabled=true  # Deve ser true
```

**Verificação 2**: Checar logs
```bash
mvn -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG spring-boot:run
```

**Verificação 3**: Verificar endpoint
```bash
curl -v http://localhost:8080/api/auth/login
# Deve mostrar X-RateLimit-Remaining header
```

### Problema: Rate limit afeta diferentes usuários/IPs

**Verificar isolamento**: Cada usuário/IP tem seu próprio bucket
```java
// Deve retornar:
userBuckets.size() >= 1  // Diferentes usuários
ipBuckets.size() >= 1    // Diferentes IPs
```

---

## 🔗 Recursos Úteis

- Documentação Completa: `docs/PHASE4_RATE_LIMITING_COMPLETA.md`
- Bucket4j Repo: https://github.com/vladimir-bukhtoyarov/bucket4j
- RFC 6585 (HTTP 429): https://tools.ietf.org/html/rfc6585

---

## 📞 Próximos Passos

**Fase 5**: Integração com Redis para Rate Limiting distribuído
- Permite múltiplas instâncias compartilhar limites
- Persistência entre restarts
- Melhor para ambiente de produção

---

**Última Atualização**: 2 de Maio de 2026  
**Status**: ✅ Implementado e Testado

