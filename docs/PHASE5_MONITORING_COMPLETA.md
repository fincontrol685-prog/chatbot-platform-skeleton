# 📊 Phase 5 - Monitoring & Observability com Prometheus/Grafana - COMPLETA

## Status: ✅ IMPLEMENTADO E TESTADO

**Data**: 2 de Maio de 2026  
**Objetivo**: Exportar métricas de cache e rate limiting para Prometheus/Grafana  
**Tecnologia**: Micrometer + Prometheus + Structured JSON Logging  

---

## 📈 Arquitetura de Monitoramento

```
┌────────────────────────────────────┐
│    Application Metrics             │
├────────────────────────────────────┤
│ ├─ Rate Limiting Metrics           │
│ │  ├─ violations.total             │
│ │  ├─ allowed.total                │
│ │  ├─ check.duration               │
│ │  └─ remaining.tokens             │
│ ├─ Cache Metrics                   │
│ │  ├─ hits.total                   │
│ │  ├─ misses.total                 │
│ │  ├─ evictions.total              │
│ │  ├─ access.time                  │
│ │  ├─ hit.rate                     │
│ │  └─ size                         │
│ └─ System Metrics (JVM)            │
│    ├─ Memory                       │
│    ├─ CPU                          │
│    └─ Threads                      │
└────────┬───────────────────────────┘
         │
         ▼
┌────────────────────────────────────┐
│  /actuator/prometheus              │
│  (Prometheus Scrape Endpoint)      │
└────────┬───────────────────────────┘
         │
         ▼
┌────────────────────────────────────┐
│    Prometheus                      │
│    (Time-Series Database)          │
│    Scrapes: http://localhost:8080/ │
│    actuator/prometheus?            │
└────────┬───────────────────────────┘
         │
         ▼
┌────────────────────────────────────┐
│    Grafana                         │
│    (Visualization & Dashboards)    │
│    Port: 3000                      │
│    Data Source: Prometheus         │
└────────────────────────────────────┘
```

---

## 📁 Arquivos Criados

### 1. **Metrics Classes (2 arquivos)**
```
src/main/java/com/br/chatbotplatformskeleton/metrics/
├── RateLimitingMetrics.java        (85 linhas)
│   └─ Rastreia violações, requisições permitidas, duração de checks
└── CachingMetrics.java             (130 linhas)
    └─ Rastreia hits, misses, evictions, tempo de acesso, hit rate
```

### 2. **Configuration (1 arquivo)**
```
src/main/java/com/br/chatbotplatformskeleton/config/
└── MonitoringConfiguration.java    (29 linhas)
    └─ Expõe endpoints de Prometheus, health, metrics
```

### 3. **Logging Utilities (1 arquivo)**
```
src/main/java/com/br/chatbotplatformskeleton/util/
└── StructuredLogger.java           (91 linhas)
    └─ Logging estruturado em JSON para todos os eventos
```

### 4. **Tests (3 arquivos)**
```
src/test/java/com/br/chatbotplatformskeleton/
├── metrics/
│   ├── RateLimitingMetricsTest.java (81 linhas, 5 testes)
│   └── CachingMetricsTest.java      (111 linhas, 7 testes)
└── util/
    └── StructuredLoggerTest.java    (97 linhas, 7 testes)

TOTAL: 19 testes, 289 linhas, 0 failures ✅
```

### 5. **Configuration Updated**
```
src/main/resources/application.properties
├─ management.endpoints.web.exposure.include
├─ management.endpoint.health.show-details
├─ management.metrics.export.prometheus.enabled
├─ logging.level configuration
└─ logging.pattern configuration
```

---

## 🎯 Métricas Expostas

### Rate Limiting Metrics

| Métrica | Tipo | Descrição |
|---------|------|-----------|
| `ratelimit.violations.total` | Counter | Total de violações de rate limit |
| `ratelimit.allowed.total` | Counter | Total de requisições permitidas |
| `ratelimit.check.duration` | Timer | Tempo de execução de rate limit check (p50, p95, p99) |
| `ratelimit.remaining.tokens.{type}` | Gauge | Tokens restantes por tipo |

### Caching Metrics

| Métrica | Tipo | Descrição |
|---------|------|-----------|
| `cache.hits.total` | Counter | Total de cache hits |
| `cache.misses.total` | Counter | Total de cache misses |
| `cache.evictions.total` | Counter | Total de evictions (TTL) |
| `cache.access.time` | Timer | Tempo de acesso ao cache (p50, p95, p99) |
| `cache.hit.rate.{cacheName}` | Gauge | Taxa de hit do cache (%) |
| `cache.size.{cacheName}` | Gauge | Tamanho atual do cache |

### System Metrics (Auto)

| Métrica | Descrição |
|---------|-----------|
| `jvm_memory_used` | Memória JVM usada |
| `jvm_memory_max` | Memória JVM máxima |
| `process_cpu_usage` | CPU usage do processo |
| `logback_events_total` | Total de eventos de log |

---

## 🚀 Actuator Endpoints

### Disponibilizados (via management.properties)

```
GET /actuator/health
  → Application health status (UP/DOWN)
  → Componentes: DB, Cache, etc.

GET /actuator/metrics
  → Lista de todas as métricas disponíveis

GET /actuator/metrics/{metric.name}
  → Detalhes de uma métrica específica
  → Ex: /actuator/metrics/ratelimit.violations.total

GET /actuator/prometheus
  → Formato Prometheus para scraping
  → Usado por Prometheus para coletar métricas

GET /actuator/info
  → Informações da aplicação
  → Nome, versão, etc.

GET /actuator/env
  → Variáveis de ambiente e propriedades
```

---

## 📊 Configuração (application.properties)

```properties
# Actuator Endpoints
management.endpoints.web.exposure.include=health,metrics,prometheus,info,env
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always
management.endpoint.info.enabled=true

# Prometheus Metrics Export
management.metrics.export.prometheus.enabled=true
management.metrics.enable.jvm=true
management.metrics.enable.process=true
management.metrics.enable.system=true
management.metrics.enable.logback=true

# Metric Tags
management.metrics.tags.application=chatbot-platform-skeleton
management.metrics.tags.environment=development

# Logging
logging.level.root=INFO
logging.level.com.br.chatbotplatformskeleton=DEBUG
logging.file.name=logs/chatbot-platform.log
logging.file.max-size=10MB
logging.file.max-history=10
```

---

## 📝 Structured Logging (JSON)

### Exemplo de Log Estruturado

```json
{
  "timestamp": "2026-05-03T02:39:21.838Z",
  "event_type": "RATE_LIMIT",
  "action": "DENIED",
  "identifier": "user123",
  "identifier_type": "USER",
  "remaining_tokens": 0
}
```

### Tipos de Eventos

1. **RATE_LIMIT** - Violações e aprovações
   ```json
   {"event_type": "RATE_LIMIT", "action": "DENIED", "identifier": "user123", ...}
   ```

2. **CACHE** - Hits, misses, evictions
   ```json
   {"event_type": "CACHE", "action": "HIT", "cache_name": "botConfig", ...}
   ```

3. **PERFORMANCE** - Operações e latência
   ```json
   {"event_type": "PERFORMANCE", "operation": "processRequest", "duration_ms": 150, ...}
   ```

4. **SECURITY** - Eventos de segurança
   ```json
   {"event_type": "SECURITY", "event_name": "SUSPICIOUS_ACTIVITY", "severity": "HIGH", ...}
   ```

---

## 🧪 Testes Implementados

### RateLimitingMetricsTest (5 testes)
✅ Record rate limit violations  
✅ Record allowed requests  
✅ Record check duration  
✅ Record remaining tokens  
✅ Metric separation by type  

### CachingMetricsTest (7 testes)
✅ Record cache hits  
✅ Record cache misses  
✅ Record cache evictions  
✅ Record access time  
✅ Record cache size  
✅ Calculate hit rate  
✅ Metric separation by cache  

### StructuredLoggerTest (7 testes)
✅ Log structured events  
✅ Log rate limit events  
✅ Log cache events  
✅ Log performance events  
✅ Log security events  
✅ Handle null context  
✅ Include timestamp  

**Total**: 19 testes, 100% passing ✅

---

## 🔧 Como Usar

### 1. Acessar Prometheus Endpoint

```bash
# Ver todas as métricas disponíveis
curl http://localhost:8080/actuator/metrics

# Ver métrica específica (JSON)
curl http://localhost:8080/actuator/metrics/ratelimit.violations.total

# Prometheus format
curl http://localhost:8080/actuator/prometheus
```

### 2. Setup Prometheus (docker-compose)

```yaml
version: '3.8'
services:
  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'

  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
```

### 3. Prometheus Configuration (prometheus.yml)

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'chatbot-platform'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
```

### 4. Grafana Dashboard

1. Open Grafana: http://localhost:3000
2. Login: admin / admin
3. Add Prometheus datasource: http://prometheus:9090
4. Create dashboard com queries:

```
Rate Limiting Panel:
- Query: rate(ratelimit.violations.total[1m])
- Title: "Violations per minute"

Cache Hit Rate Panel:
- Query: cache.hit.rate.botConfig
- Title: "Bot Config Cache Hit Rate"

Memory Usage Panel:
- Query: jvm_memory_used_bytes / jvm_memory_max_bytes
- Title: "JVM Memory Usage"
```

### 5. Health Check

```bash
curl http://localhost:8080/actuator/health

# Response:
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "redis": {"status": "UP"},
    "ping": {"status": "UP"}
  }
}
```

---

## 📈 Monitoramento de Performance

### Métricas Recomendadas

1. **Taxa de Erro**
   - `ratelimit.violations.total` / total requests
   - Alerta: > 5% em 5 minutos

2. **Cache Effectiveness**
   - `cache.hit.rate.botConfig`
   - Target: > 40%

3. **Latência**
   - `ratelimit.check.duration` p95
   - Target: < 5ms

4. **Sistema**
   - `jvm_memory_used_bytes`
   - `process_cpu_usage`
   - Alerta: Memory > 80%

---

## 🛠️ Integração com Phase 3 & 4

```
Phase 3: Caching com Redis
  ├─ @Cacheable metrics
  └─ Integração: CachingMetrics rastreia hits/misses

Phase 4: Rate Limiting com Bucket4j
  ├─ Rate limit violations
  └─ Integração: RateLimitingMetrics rastreia attempts

Phase 5: Monitoring (NOVO)
  ├─ RateLimitingMetrics
  ├─ CachingMetrics
  ├─ Structured Logging em JSON
  └─ Expõe via /actuator/prometheus
```

---

## ✨ Features Implementadas

✅ **Prometheus Metrics** - Counters, Timers, Gauges  
✅ **Custom Metrics** - Rate limiting & caching specific  
✅ **Actuator Endpoints** - Health, metrics, prometheus  
✅ **Structured Logging** - JSON para todos eventos  
✅ **Tag-based Metrics** - Por tipo e identificador  
✅ **Percentile Tracking** - p50, p95, p99  
✅ **JVM Metrics** - Memory, CPU, threads  
✅ **Configuration** - Via application.properties  
✅ **19 Tests** - Cobertura abrangente  

---

## 🚀 Executar

### 1. Build & Test

```bash
cd /home/robertojr/chatbot-platform-skeleton

# Compilar
mvn clean compile

# Testar Phase 5
mvn test -Dtest=RateLimitingMetricsTest,CachingMetricsTest,StructuredLoggerTest

# Resultado: 19 tests, 0 failures ✅
```

### 2. Iniciar Aplicação

```bash
mvn spring-boot:run

# Accesso:
# - Application: http://localhost:8080
# - Prometheus: http://localhost:8080/actuator/prometheus
# - Health: http://localhost:8080/actuator/health
# - Metrics: http://localhost:8080/actuator/metrics
```

### 3. Testar Métricas

```bash
# Ver health
curl http://localhost:8080/actuator/health | jq

# Ver métricas de rate limiting
curl http://localhost:8080/actuator/metrics/ratelimit.violations.total | jq

# Ver métricas de cache
curl http://localhost:8080/actuator/metrics/cache.hits.total | jq

# Formato Prometheus
curl http://localhost:8080/actuator/prometheus | head -30
```

---

## 📊 Queries Prometheus Úteis

```promql
# Taxa de violações por minuto
rate(ratelimit.violations.total[1m])

# Taxa de cache hit
cache.hits.total / (cache.hits.total + cache.misses.total)

# Latência p95 de rate limit check
ratelimit_check_duration{quantile="0.95"}

# Memória JVM
jvm_memory_used_bytes / jvm_memory_max_bytes

# Requisições permitidas por segundo
rate(ratelimit.allowed.total[1m])
```

---

## 🎯 Próximas Melhorias (Phase 6+)

❌ **Custom Dashboards** - Grafana templates prontos  
❌ **Alerting Rules** - Prometheus alerts (high CPU, etc)  
❌ **Distributed Tracing** - Jaeger/OpenTelemetry  
❌ **Log Aggregation** - ELK stack ou Splunk  
❌ **SLA Monitoring** - Tracking de SLOs  
❌ **Anomaly Detection** - ML-based detection  

---

## 📝 Documentação

- Micrometer: https://micrometer.io/
- Prometheus: https://prometheus.io/docs/
- Grafana: https://grafana.com/docs/grafana/latest/
- Spring Actuator: https://spring.io/guides/gs/spring-boot-docker/

---

**Implementação Concluída**: Phase 5 ✅  
**Data**: 2 de Maio de 2026  
**Status**: READY FOR MONITORING 🚀

