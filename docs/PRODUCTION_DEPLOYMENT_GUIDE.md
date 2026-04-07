# 🚀 PRODUCTION DEPLOYMENT GUIDE
## Chatbot Platform - Step-by-Step Deployment

---

## 📋 PRÉ-REQUISITOS

### Hardware Mínimo (Recomendado)
- **CPU:** 4 cores
- **RAM:** 8 GB (16 GB recomendado)
- **Storage:** 50 GB SSD
- **Rede:** 100 Mbps

### Software Obrigatório
- Docker 20.10+
- Docker Compose 2.0+
- Git
- OpenSSL (para certificados SSL)

### Credenciais e Secrets
- JWT Secret (mínimo 32 caracteres)
- Database password
- SSL certificates (para produção)

---

## 🔒 FASE 1: PREPARAÇÃO DE SEGURANÇA

### 1.1 Gerar Secrets
```bash
# Generate JWT Secret
JWT_SECRET=$(openssl rand -base64 32)
echo "JWT_SECRET=$JWT_SECRET"

# Generate Database Password
DB_PASSWORD=$(openssl rand -base64 24)
echo "DB_PASSWORD=$DB_PASSWORD"

# Generate Grafana Password
GRAFANA_PASSWORD=$(openssl rand -base64 16)
echo "GRAFANA_PASSWORD=$GRAFANA_PASSWORD"
```

### 1.2 Criar arquivo .env
```bash
cp .env.example .env
chmod 600 .env

# Edit .env with your secrets
vim .env
```

**IMPORTANTE:** Restrinja as permissões do arquivo:
```bash
# Apenas owner pode ler
chmod 600 .env

# Verificar permissões
ls -la .env
```

### 1.3 Configurar SSL/TLS (Opcional mas Recomendado)
```bash
# Criar diretório para certificados
mkdir -p ssl

# Para desenvolvimento: criar certificado auto-assinado
openssl req -x509 -newkey rsa:4096 -keyout ssl/key.pem -out ssl/cert.pem -days 365 -nodes

# Para produção: usar certificado de autoridade certificadora (CA)
# Exemplo com Let's Encrypt/Certbot
certbot certonly --standalone -d chatbot.company.com
cp /etc/letsencrypt/live/chatbot.company.com/fullchain.pem ssl/cert.pem
cp /etc/letsencrypt/live/chatbot.company.com/privkey.pem ssl/key.pem
```

### 1.4 Configurar Variáveis de Ambiente
```bash
# Adicione ao .env
SSL_ENABLED=true
CORS_ORIGINS=https://chatbot.company.com,https://www.company.com
```

---

## 🏗️ FASE 2: BUILD E VALIDAÇÃO

### 2.1 Build Backend
```bash
# Compilar backend
mvn clean package -DskipTests

# Validar build
ls -lah target/chatbot-platform-skeleton-*.jar
```

### 2.2 Build Frontend (Opcional)
```bash
cd frontend
npm install
npm run build

# Validar build
ls -lah dist/
```

### 2.3 Testes
```bash
# Executar testes unitários
mvn test

# Coverage
mvn jacoco:report
open target/site/jacoco/index.html

# Validar cobertura mínima (80%)
```

---

## 📦 FASE 3: CONTAINERIZAÇÃO

### 3.1 Build Docker Image
```bash
# Build backend image
docker build -f Dockerfile.backend -t chatbot-backend:1.0.0 .

# Validar imagem
docker images | grep chatbot-backend
```

### 3.2 Verificar Segurança da Imagem
```bash
# Scan for vulnerabilities (usando Trivy, se instalado)
trivy image chatbot-backend:1.0.0

# Validar que não usa root
docker inspect chatbot-backend:1.0.0 | grep -i user
```

---

## 🐳 FASE 4: DEPLOYMENT COM DOCKER COMPOSE

### 4.1 Preparar Estrutura de Diretórios
```bash
# Criar diretórios necessários
mkdir -p monitoring/prometheus
mkdir -p monitoring/grafana/dashboards
mkdir -p monitoring/grafana/datasources
mkdir -p ssl
mkdir -p db/migrations
```

### 4.2 Criar Configurações de Monitoramento
```bash
# Copiar/criar prometheus.yml
cat > monitoring/prometheus.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'chatbot-backend'
    static_configs:
      - targets: ['backend:8080']
    metrics_path: '/actuator/prometheus'
EOF
```

### 4.3 Iniciar Stack
```bash
# Load environment variables
source .env

# Start containers
docker-compose up -d

# Wait for services to be ready
sleep 30

# Validar status
docker-compose ps
```

### 4.4 Verificar Health
```bash
# Check backend health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP","components":{"db":{"status":"UP"},...}}

# Check database connection
curl http://localhost:8080/actuator/db

# Check metrics
curl http://localhost:8080/actuator/prometheus | head -20
```

---

## ✅ FASE 5: TESTES PÓS-DEPLOYMENT

### 5.1 Teste de Conectividade
```bash
# Backend
curl -v http://localhost:8080/actuator/health

# Database
docker exec chatbot-mysql mysql -h localhost -u chatbot_user -p -e "SHOW DATABASES;"

# Nginx (quando configurado)
curl -v http://localhost
```

### 5.2 Teste de Login
```bash
# Obter token JWT
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' | jq .

# Guardar token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

echo "Token: $TOKEN"
```

### 5.3 Teste de Segurança
```bash
# Verificar headers de segurança
curl -i http://localhost:8080/actuator/health | grep -E "X-Frame-Options|X-Content-Type|X-XSS|Strict-Transport"

# Teste CORS
curl -i -H "Origin: https://chatbot.company.com" http://localhost:8080/api/auth/login

# Teste de rate limiting
for i in {1..1010}; do curl -s http://localhost:8080/actuator/health > /dev/null; done
curl -i http://localhost:8080/actuator/health  # Should return 429 after limit
```

### 5.4 Teste de Performance
```bash
# Usar Apache Bench
ab -n 1000 -c 10 http://localhost:8080/actuator/health

# Usar wrk
wrk -t4 -c100 -d30s http://localhost:8080/actuator/health
```

---

## 📊 FASE 6: MONITORAMENTO

### 6.1 Acessar Dashboards
- **Backend Health:** http://localhost:8080/actuator/health
- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3000 (admin / senha do .env)

### 6.2 Verificar Logs
```bash
# Logs do backend
docker logs -f chatbot-backend

# Logs do MySQL
docker logs -f chatbot-mysql

# Logs do Nginx
docker logs -f chatbot-nginx

# Logs com timestamp
docker logs --timestamps chatbot-backend | tail -100
```

### 6.3 Monitorar Recursos
```bash
# CPU e Memory
docker stats chatbot-backend

# Disk usage
du -sh ./*
docker exec chatbot-mysql du -sh /var/lib/mysql
```

---

## 🔄 FASE 7: MANUTENÇÃO DIÁRIA

### 7.1 Backup do Database
```bash
# Backup manual
docker exec chatbot-mysql mysqldump -h localhost -u chatbot_user -p \
  --all-databases > backup-$(date +%Y%m%d).sql

# Backup automático (cron job)
# Add to crontab -e:
# 0 2 * * * docker exec chatbot-mysql mysqldump -h localhost -u chatbot_user -p \
#   --all-databases > /backups/backup-$(date +\%Y\%m\%d).sql
```

### 7.2 Verificação de Saúde
```bash
#!/bin/bash
# health-check.sh

echo "Checking backend health..."
STATUS=$(curl -s http://localhost:8080/actuator/health | jq .status)
if [ "$STATUS" != '"UP"' ]; then
  echo "WARNING: Backend is not healthy!"
  # Send alert (email, Slack, PagerDuty, etc)
fi

echo "Checking database connection..."
docker exec chatbot-mysql mysql -h localhost -u chatbot_user -p -e "SELECT 1" || {
  echo "ERROR: Database connection failed!"
}
```

### 7.3 Cleanup de Logs e Cache
```bash
# Limpar logs docker
docker system prune -f --volumes

# Limpar cache de imagens
docker image prune -a -f

# Manter últimos 7 dias de logs
docker exec chatbot-mysql mysql -h localhost -u chatbot_user -p -e \
  "DELETE FROM audit_log WHERE created_at < DATE_SUB(NOW(), INTERVAL 7 DAY);"
```

---

## 🚨 TROUBLESHOOTING

### Problema: Backend não inicia
```bash
# Verificar logs
docker logs chatbot-backend

# Verificar configurações
echo $JWT_SECRET
echo $DB_PASSWORD

# Reiniciar serviço
docker-compose restart backend
```

### Problema: Database não conecta
```bash
# Verificar status
docker logs chatbot-mysql

# Verificar credenciais
docker exec chatbot-mysql mysql -u root -p -e "SHOW USERS;"

# Reset de credenciais
docker-compose down -v  # Remove volumes!
docker-compose up -d
```

### Problema: Rate limiting muito agressivo
```bash
# Ajustar no SecurityHeadersConfig.java
# Aumentar MAX_REQUESTS_PER_MINUTE
# Recompilar e fazer redeploy
```

### Problema: SSL Certificate expirado
```bash
# Renovar certificado
certbot renew

# Copiar novo certificado
cp /etc/letsencrypt/live/chatbot.company.com/fullchain.pem ssl/cert.pem
cp /etc/letsencrypt/live/chatbot.company.com/privkey.pem ssl/key.pem

# Recarregar Nginx
docker exec chatbot-nginx nginx -s reload
```

---

## 📈 ESCALABILIDADE FUTURA

### Quando escalar horizontalmente:
1. Usar Docker Swarm ou Kubernetes
2. Adicionar Load Balancer (HAProxy, AWS ELB)
3. Centralizar sessions (Redis)
4. Replicar database (MySQL Replication, Galera Cluster)
5. CDN para assets estáticos

### Exemplo com Kubernetes:
```bash
# Criar manifests em k8s/
# - deployment.yaml
# - service.yaml
# - configmap.yaml
# - secret.yaml
# - hpa.yaml (Auto-scaling)

# Deploy
kubectl apply -f k8s/
```

---

## 📞 SUPORTE E ESCALAÇÃO

- **Application Issues:** Dev Team
- **Database Issues:** DBA Team
- **Infrastructure Issues:** DevOps Team
- **Security Issues:** Security Team
- **Performance Issues:** SRE Team

---

## ✅ CHECKLIST PRÉ-PRODUÇÃO FINAL

- [ ] Todas as secrets alteradas
- [ ] SSL/TLS configurado
- [ ] Backups testados
- [ ] Monitoring ativo
- [ ] Logs centralizados
- [ ] Testes de carga executados
- [ ] Disaster recovery testado
- [ ] Runbooks documentados
- [ ] Equipe treinada
- [ ] Aprovação de stakeholders

---

**Documento Versão:** 1.0  
**Data:** 12 de Março de 2026  
**Próxima Revisão:** Após primeira produção

