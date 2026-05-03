# 🚀 Guia Rápido - Monitoring & Observability (Fase 5)

## O que foi implementado?

### ✅ Métricas de Rate Limiting
- Total de violações
- Requisições permitidas
- Duração de checks (p50, p95, p99)
- Tokens restantes

### ✅ Métricas de Cache
- Cache hits/misses
- Taxa de hits (%)
- Tempo de acesso
- Evictions por TTL
- Tamanho do cache

### ✅ Logging Estruturado
- JSON logs para todos eventos
- Tipos: RATE_LIMIT, CACHE, PERFORMANCE, SECURITY
- Timestamp automático

### ✅ Actuator Endpoints
- `/actuator/health` - Health status
- `/actuator/metrics` - Lista de métricas
- `/actuator/prometheus` - Prometheus scrape endpoint
- `/actuator/info` - Informações app

---

## 📊 Testar em 3 Passos

### 1. Compilar e Testar

```bash
cd /home/robertojr/chatbot-platform-skeleton

# Build
mvn clean compile

# Tests (19 testes, 0 failures)
mvn test -Dtest=RateLimitingMetricsTest,CachingMetricsTest,StructuredLoggerTest
```

### 2. Iniciar Aplicação

```bash
mvn spring-boot:run

# Aguarde: "Application started"
```

### 3. Acessar Métricas

```bash
# Health check
curl http://localhost:8080/actuator/health

# Listar todas métricas
curl http://localhost:8080/actuator/metrics

# Métricas specific
curl http://localhost:8080/actuator/metrics/ratelimit.violations.total

# Prometheus format (para Grafana)
curl http://localhost:8080/actuator/prometheus
```

---

## 🔧 Setup Completo (Docker)

### Passo 1: docker-compose.yml

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_REDIS_HOST=redis
    depends_on:
      - redis

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

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

### Passo 2: prometheus.yml

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'chatbot-platform'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
```

### Passo 3: Iniciar

```bash
docker-compose up -d

# Acessar:
# - Prometheus: http://localhost:9090
# - Grafana: http://localhost:3000 (admin/admin)
```

---

## 📈 Grafana Setup

### 1. Add Prometheus DataSource

```
- URL: http://prometheus:9090
- Name: Prometheus
- Save & Test
```

### 2. Criar Dashboard

Panel 1: Rate Limit Violations
```
Query: rate(ratelimit.violations.total[1m])
Type: Graph
Title: Violations per Minute
```

Panel 2: Cache Hit Rate
```
Query: cache.hits.total / (cache.hits.total + cache.misses.total) * 100
Type: Gauge
Title: Cache Hit Rate (%)
Target: 40%
```

Panel 3: JVM Memory
```
Query: jvm_memory_used_bytes / jvm_memory_max_bytes * 100
Type: Gauge
Title: JVM Memory Usage (%)
```

---

## 🎯 Métricas Importantes

### Taxa de Abuso (Alert)
```
rate(ratelimit.violations.total[5m]) > 0.05
```

### Performance Degradation
```
ratelimit_check_duration{quantile="0.95"} > 5
```

### Cache Ineffectiveness
```
(cache.hits.total / (cache.hits.total + cache.misses.total)) < 0.3
```

### Memory Leak Detection
```
jvm_memory_used_bytes > jvm_memory_max_bytes * 0.8
```

---

## 📁 Arquivos Criados

### Código (4 arquivos)
- `RateLimitingMetrics.java` (85 linhas)
- `CachingMetrics.java` (130 linhas)
- `MonitoringConfiguration.java` (29 linhas)
- `StructuredLogger.java` (91 linhas)

### Testes (3 arquivos)
- `RateLimitingMetricsTest.java` (5 testes)
- `CachingMetricsTest.java` (7 testes)
- `StructuredLoggerTest.java` (7 testes)

### Config
- `application.properties` atualizado

---

## 🔗 Referências

- Micrometer: https://micrometer.io/
- Prometheus: https://prometheus.io/
- Grafana: https://grafana.com/docs/
- Spring Actuator: https://spring.io/guides/gs/actuator-service/

---

## ✅ Próximos Passos

1. **Testar métricas** - Fazer requests e ver métricas aumentarem
2. **Setup Prometheus** - Coletar métricas
3. **Criar Grafana dashboards** - Visualizar trends
4. **Alerting** - Configurar notificações

---

**Status**: ✅ COMPLETO E TESTADO  
**Build**: ✅ SUCCESS  
**Testes**: ✅ 19/19 PASSING  
**Ready**: 🚀 SIM

