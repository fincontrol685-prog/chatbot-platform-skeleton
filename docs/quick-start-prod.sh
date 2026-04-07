#!/bin/bash
# Quick Start Guide - Chatbot Platform Production
# Copy-paste ready commands for deployment

set -e

echo "🚀 Chatbot Platform - Production Quick Start"
echo "=============================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check prerequisites
echo "📋 Checking prerequisites..."

if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker not found. Install from https://docs.docker.com/get-docker/${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ Docker Compose not found. Install from https://docs.docker.com/compose/install/${NC}"
    exit 1
fi

if ! command -v openssl &> /dev/null; then
    echo -e "${RED}❌ OpenSSL not found. Please install openssl${NC}"
    exit 1
fi

echo -e "${GREEN}✓ All prerequisites found${NC}"
echo ""

# Step 1: Generate Secrets
echo "🔒 Step 1: Generating Security Secrets..."
JWT_SECRET=$(openssl rand -base64 32)
DB_PASSWORD=$(openssl rand -base64 24)
MYSQL_ROOT_PASSWORD=$(openssl rand -base64 24)
GRAFANA_PASSWORD=$(openssl rand -base64 16)

echo "  - JWT Secret: ${JWT_SECRET:0:16}..."
echo "  - DB Password: ${DB_PASSWORD:0:16}..."
echo "  - MySQL Root: ${MYSQL_ROOT_PASSWORD:0:16}..."
echo "  - Grafana: ${GRAFANA_PASSWORD:0:16}..."
echo ""

# Step 2: Create .env file
echo "📝 Step 2: Creating .env file..."
cat > .env << EOF
# Generated on $(date)

# Database
DB_HOST=mysql
DB_PORT=3306
DB_NAME=chatbot_prod
DB_USERNAME=chatbot_user
DB_PASSWORD=$DB_PASSWORD
MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD

# JWT
JWT_SECRET=$JWT_SECRET
JWT_EXPIRATION_MS=3600000

# CORS
CORS_ORIGINS=http://localhost:4200,http://localhost

# Server
SERVER_PORT=8080
BACKEND_PORT=8080
NGINX_PORT=80

# Monitoring
PROMETHEUS_PORT=9090
GRAFANA_PORT=3000
GRAFANA_PASSWORD=$GRAFANA_PASSWORD

# Environment
SPRING_PROFILES_ACTIVE=prod
LOGGING_LEVEL_ROOT=INFO
EOF

chmod 600 .env
echo -e "${GREEN}✓ .env file created (permissions: 600)${NC}"
echo ""

# Step 3: Create directories
echo "📁 Step 3: Creating required directories..."
mkdir -p monitoring/prometheus
mkdir -p monitoring/grafana/dashboards
mkdir -p monitoring/grafana/datasources
mkdir -p ssl
mkdir -p db/migrations

echo -e "${GREEN}✓ Directories created${NC}"
echo ""

# Step 4: Create Prometheus config
echo "⚙️  Step 4: Creating Prometheus configuration..."
cat > monitoring/prometheus.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'chatbot-backend'
    static_configs:
      - targets: ['backend:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 10s
EOF

echo -e "${GREEN}✓ Prometheus config created${NC}"
echo ""

# Step 5: Build Backend (Optional)
read -p "🔨 Build backend? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Building backend with Maven..."
    mvn clean package -DskipTests -q
    echo -e "${GREEN}✓ Backend build successful${NC}"
fi
echo ""

# Step 6: Start Services
echo "🐳 Step 6: Starting Docker services..."
docker-compose up -d

echo -e "${GREEN}✓ Services started${NC}"
echo ""

# Step 7: Wait for services
echo "⏳ Waiting for services to be healthy..."
sleep 10

# Step 8: Health checks
echo "🏥 Step 8: Running health checks..."
echo ""

# Backend health
echo "Checking Backend..."
if curl -s http://localhost:8080/actuator/health | grep -q '"status":"UP"'; then
    echo -e "${GREEN}✓ Backend: UP${NC}"
else
    echo -e "${YELLOW}⚠ Backend: Starting...${NC}"
    sleep 5
fi

# Database health
echo "Checking Database..."
if docker exec chatbot-mysql mysql -u chatbot_user -p"$DB_PASSWORD" -e "SELECT 1" &> /dev/null; then
    echo -e "${GREEN}✓ Database: UP${NC}"
else
    echo -e "${RED}❌ Database: DOWN${NC}"
fi

# Prometheus health
echo "Checking Prometheus..."
if curl -s http://localhost:9090/-/healthy &> /dev/null; then
    echo -e "${GREEN}✓ Prometheus: UP${NC}"
else
    echo -e "${YELLOW}⚠ Prometheus: Starting...${NC}"
fi

echo ""
echo "✅ Setup Complete!"
echo ""
echo "📊 Access your services:"
echo "  - Backend:    http://localhost:8080"
echo "  - Health:     http://localhost:8080/actuator/health"
echo "  - Metrics:    http://localhost:8080/actuator/prometheus"
echo "  - Prometheus: http://localhost:9090"
echo "  - Grafana:    http://localhost:3000 (admin / $(echo $GRAFANA_PASSWORD | cut -c1-8)...)"
echo "  - Nginx:      http://localhost"
echo ""
echo "🔐 Your secrets are stored in .env (keep safe!)"
echo ""
echo "📖 Next steps:"
echo "  1. Review .env file"
echo "  2. Configure SSL/TLS (see PRODUCTION_DEPLOYMENT_GUIDE.md)"
echo "  3. Test endpoints: curl http://localhost:8080/actuator/health"
echo "  4. Read PRODUCTION_DEPLOYMENT_GUIDE.md for complete setup"
echo ""

