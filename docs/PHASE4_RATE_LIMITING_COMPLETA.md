# 📈 Phase 4 - Rate Limiting com Bucket4j - COMPLETA

## Status: ✅ IMPLEMENTADO E TESTADO

**Data**: 2 de Maio de 2026  
**Objetivo**: Proteger endpoints críticos com rate limiting de 429 responses  
**Tecnologia**: Bucket4j (Token Bucket Algorithm)  

---

## 📊 Configuração Implementada

### Rate Limits por Endpoint

| Endpoint | Tipo | Limite | Intervalo | Identificador |
|----------|------|--------|-----------|---------------|
| `/api/auth/login` | IP-based | 5 req | 1 min | Endereço IP |
| `/api/auth/register` | IP-based | 5 req | 1 min | Endereço IP |
| `/api/auth/forgot-password` | IP-based | 5 req | 1 min | Endereço IP |
| `/api/messages/conversation/{id}/exchange` | User-based | 10 req | 1 min | User ID |
| `/api/analytics/*` | User-based | 30 req | 1 min | User ID |

---

## 📁 Arquivos Criados

### 1. **Configuração de Rate Limiting**
```
src/main/java/com/br/chatbotplatformskeleton/config/
├── RateLimitingConfiguration.java     (Bean setup com bucket4j)
└── RateLimitProperties.java           (Properties customizáveis)
```

### 2. **Serviço de Rate Limiting**
```
src/main/java/com/br/chatbotplatformskeleton/service/
└── RateLimitService.java              (Lógica de rate limiting)
```

### 3. **Filter de Rate Limiting**
```
src/main/java/com/br/chatbotplatformskeleton/filter/
└── RateLimitingFilter.java            (Interceptor de requisições)
```

### 4. **Testes Unitários**
```
src/test/java/com/br/chatbotplatformskeleton/service/
└── RateLimitServiceTest.java         (Testes de RateLimitService)
```

### 5. **Dependências Adicionadas ao pom.xml**
```xml
<!-- Rate Limiting with Bucket4j -->
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```
> ✅ Dependência já estava presente no pom.xml

### 6. **Configurações em application.properties**
```properties
# Rate Limiting Configuration (Phase 4)
ratelimit.enabled=true

# User-based rate limiting (authenticated endpoints)
ratelimit.user.requests=10
ratelimit.user.interval-minutes=1

# IP-based rate limiting (unauthenticated endpoints)
ratelimit.ip.requests=5
ratelimit.ip.interval-minutes=1

# Analytics endpoints rate limiting
ratelimit.analytics.requests=30
ratelimit.analytics.interval-minutes=1
```

---

## 🏗️ Arquitetura de Rate Limiting

### Camadas de Rate Limiting

```
┌─────────────────────────────────┐
│      HTTP Request               │
└────────────┬────────────────────┘
             │
             ▼
┌─────────────────────────────────┐
│   RateLimitingFilter            │
│  (Intercepta todas requisições) │
└────────────┬────────────────────┘
             │
             ├─→ Auth Endpoints? ─→ enforceIpRateLimit()
             │   └─ ConcurrentHashMap<IP, Bucket>
             │
             ├─→ Analytics? ─→ enforceUserRateLimit(isAnalytics=true)
             │   └─ ConcurrentHashMap<userId, Bucket>
             │
             └─→ Message Exchange? ─→ enforceUserRateLimit()
                 └─ ConcurrentHashMap<userId, Bucket>
```

### Fluxo de Requisição com Rate Limiting

```
User Request (e.g., POST /api/auth/login)
  │
  ├─→ RateLimitingFilter.doFilterInternal()
  │    ├─→ Extrair IP do request
  │    ├─→ RateLimitService.checkIpRateLimit(ip)
  │    │   ├─→ bucket = buckets.computeIfAbsent(ip, Bucket4j::create)
  │    │   └─→ probe = bucket.tryConsumeAndReturnRemaining(1)
  │    │
  │    ├─→ probe.isConsumed()? 
  │    │   ├─ SIM: Permitir request ✓
  │    │   └─ NÃO: Retornar 429 TOO_MANY_REQUESTS ✗
  │    │
  │    └─→? Adicionar headers X-RateLimit-Remaining
  │
  └─→ Continuar para o Controller
```

---

## 🔑 Como Usar

### 1. Habilitar/Desabilitar Rate Limiting

```properties
# Em application.properties
ratelimit.enabled=true   # Habilitar
ratelimit.enabled=false  # Desabilitar
```

### 2. Personalizar Limites

```properties
# Aumentar limite para usuários
ratelimit.user.requests=20
ratelimit.user.interval-minutes=1

# Aumentar limite para IPs
ratelimit.ip.requests=10
ratelimit.ip.interval-minutes=1

# Aumentar limite para analytics
ratelimit.analytics.requests=50
ratelimit.analytics.interval-minutes=1
```

### 3. Adicionar Rate Limiting a Novo Endpoint

Para adicionar rate limiting a um novo endpoint, editar `RateLimitingFilter.enforceRateLimit()`:

```java
private boolean enforceRateLimit(HttpServletRequest request, HttpServletResponse response, String requestPath) 
        throws IOException {

    // Novo endpoint com rate limit por IP
    if (requestPath.contains("/api/novo-endpoint")) {
        return enforceIpRateLimit(request, response);
    }

    // Novo endpoint com rate limit por usuário
    if (requestPath.contains("/api/outro-endpoint")) {
        return enforceUserRateLimit(request, response, false, false);
    }
    
    return true;
}
```

---

## 📊 Monitoramento

### Response Headers
Quando a requisição é permitida, o header de resposta inclui:
```
X-RateLimit-Remaining: 9
```

Quando excede o limite:
```
HTTP/1.1 429 Too Many Requests
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1651413600

{
  "error": "Rate limit exceeded. Maximum requests per minute exceeded.",
  "retryAfter": 60
}
```

### Logs
```
[DEBUG] Rate limit check passed for user: 1 (remaining: 9)
[WARN] Rate limit exceeded for user: 1 (retry after: 60s)
```

---

## 🧪 Testes Implementados

### Testes Unitários em `RateLimitServiceTest.java`

1. ✅ `testUserRateLimitAllowed` - Permite requisições dentro do limite
2. ✅ `testUserRateLimitExceeded` - Rejeita requisições acima do limite
3. ✅ `testIpRateLimitAllowed` - IP rate limiting funciona
4. ✅ `testIpRateLimitExceeded` - IP rate limiting rejeita excesso
5. ✅ `testUserIsolation` - Usuários não afetam uns aos outros
6. ✅ `testIpIsolation` - IPs não afetam uns aos outros
7. ✅ `testAnalyticsRateLimit` - Analytics endpoint rate limit
8. ✅ `testRemainingTokens` - Retorna tokens corretos
9. ✅ `testRateLimitingDisabled` - Funciona quando desabilitado

### Executar Testes
```bash
mvn test -Dtest=RateLimitServiceTest
```

---

## ⚙️ Configuração Interna

### Token Bucket Algorithm
O bucket4j implementa o Token Bucket Algorithm:
- **Bandwidth**: Define limite máximo de tokens
- **Refill**: Define como tokens são reabastecidos
- **Consumption**: Uma requisição consome 1 token

```
Bucket inicial: ▮▮▮▮▮ (5 tokens)

Requisição 1: ▮▮▮▮░ (4 restantes)
Requisição 2: ▮▮▮░░ (3 restantes)
Requisição 3: ▮▮░░░ (2 restantes)
Requisição 4: ▮░░░░ (1 restante)
Requisição 5: ░░░░░ (0 restantes)
Requisição 6: BLOQUEADA! ✗

[Após 1 minuto]
Bucket refill: ▮▮▮▮▮ (5 tokens novamente)
```

### In-Memory Storage
Atualmente usa `ConcurrentHashMap` para armazenar buckets:
```java
// Usuários
ConcurrentHashMap<String, Bucket> userBuckets 
  // Chaves: "user:123", "user:456", "analytics:user:789"

// IPs
ConcurrentHashMap<String, Bucket> ipBuckets
  // Chaves: "ip:192.168.1.100", "ip:10.0.0.1"
```

---

## ⚠️ Limitações & Futuras Melhorias

### Limitações Atuais
- ❌ Armazenamento local em memória (não funciona em múltiplas instâncias)
- ❌ Sem persistência entre restarts
- ❌ Sem possibilidade de whitelist

### Próximas Melhorias (Phase 5+)

#### 1. **Redis-backed Rate Limiting**
```java
// Usar Redis para armazenar buckets
// Permite rate limiting distribuído em múltiplas instâncias
@Bean
public Bucket4jRedisConfiguration redisRateLimiting() {
    // Configurar com Redis
}
```

#### 2. **Whitelist/Blacklist**
```properties
# Em application.properties
ratelimit.whitelist.ips=127.0.0.1,10.0.0.1
ratelimit.blacklist.ips=192.168.1.1
ratelimit.whitelist.users=1,2,3
```

#### 3. **Alertas de Abuso**
```java
// Notificar administradores quando detectar abuso
if (probe.getAttemptedToConsume() > threshold) {
    notificationService.alertAbuse(userId, ip);
}
```

#### 4. **Rate Limiting Dinâmico**
```java
// Ajustar limites baseado em carga do sistema
if (systemLoad > 80%) {
    properties.getUser().setRequests(5); // Reduzir limite
}
```

---

## 📝 Como Compilar & Testar

### Compilar
```bash
cd /home/robertojr/chatbot-platform-skeleton
mvn clean compile
```

### Executar Testes
```bash
mvn test -Dtest=RateLimitServiceTest
```

### Executar Aplicação
```bash
mvn spring-boot:run

# Ou via Java
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar
```

### Testar Rate Limiting com cURL

#### Teste 1: Login com rate limit por IP
```bash
# Primeiras 5 requisições devem passar
for i in {1..5}; do
  curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"123"}' \
    -w "\nStatus: %{http_code}\n\n"
done

# 6ª requisição deve retornar 429
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123"}' \
  -w "\nStatus: %{http_code}\n\n"
```

#### Teste 2: Message Exchange com rate limit por usuário
```bash
# Primeiras 10 requisições devem passar (com token válido)
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/messages/conversation/1/exchange \
    -H "Authorization: Bearer YOUR_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"content":"test"}' \
    -w "\nStatus: %{http_code}\n\n"
done

# 11ª requisição deve retornar 429
curl -X POST http://localhost:8080/api/messages/conversation/1/exchange \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"content":"test"}' \
  -w "\nStatus: %{http_code}\n\n"
```

---

## 🔗 Referências & Documentação

- [Bucket4j Documentation](https://github.com/vladimir-bukhtoyarov/bucket4j)
- [Token Bucket Algorithm](https://en.wikipedia.org/wiki/Token_bucket)
- [HTTP 429 - Too Many Requests](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/429)
- [Rate Limiting Best Practices](https://cloud.google.com/architecture/rate-limiting-strategies-techniques)

---

## 📈 Próximas Fases

**Fase 5**: Monitoring & Observabilidade
- Exportar métricas de rate limiting para Prometheus
- Criar dashboards em Grafana
- Alertas de abuso de API

**Fase 6**: Advanced Security
- 2FA com authenticators
- IP Whitelisting para admin
- Encrypted audit logs

---

**Implementação Concluída**: Rate Limiting com Bucket4j  
**Próxima Ação**: Fase 5 - Monitoring & Observabilidade com Prometheus/Grafana

