#!/bin/bash
# Docker Compose startup com retry e health checks melhorados

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}🚀 Iniciando aplicação com Docker Compose${NC}"

# Parar containers existentes se houver
echo "Limpando containers antigos..."
docker-compose down 2>/dev/null || true
sleep 2

# Iniciar MySQL primeiro
echo -e "${YELLOW}📦 Iniciando MySQL...${NC}"
docker-compose up -d mysql

# Aguardar MySQL estar pronto
echo -e "${YELLOW}⏳ Aguardando MySQL estar saudável...${NC}"
MAX_ATTEMPTS=30
ATTEMPT=0

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    if docker-compose exec -T mysql mysqladmin ping -h localhost &> /dev/null; then
        echo -e "${GREEN}✓ MySQL está pronto!${NC}"
        break
    fi
    ATTEMPT=$((ATTEMPT + 1))
    echo -n "."
    sleep 2
done

if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
    echo -e "${RED}✗ MySQL não respondeu após $MAX_ATTEMPTS tentativas${NC}"
    docker-compose logs mysql
    exit 1
fi

# Iniciar backend
echo -e "${YELLOW}📦 Iniciando Backend...${NC}"
docker-compose up -d backend

# Aguardar backend estar pronto
echo -e "${YELLOW}⏳ Aguardando Backend estar saudável...${NC}"
MAX_ATTEMPTS=40
ATTEMPT=0

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    if curl -s http://localhost:8080/actuator/health | grep -q '"status":"UP"'; then
        echo -e "${GREEN}✓ Backend está pronto!${NC}"
        break
    fi
    ATTEMPT=$((ATTEMPT + 1))
    echo -n "."
    sleep 3
done

if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
    echo -e "${RED}✗ Backend não respondeu após $MAX_ATTEMPTS tentativas${NC}"
    echo -e "${YELLOW}Verificando logs...${NC}"
    docker-compose logs backend | tail -50
    exit 1
fi

# Iniciar serviços restantes
echo -e "${YELLOW}📦 Iniciando serviços restantes (Nginx, Prometheus, Grafana)...${NC}"
docker-compose up -d

echo ""
echo -e "${GREEN}✅ Aplicação iniciada com sucesso!${NC}"
echo ""
echo "📊 Acesse os serviços:"
echo "  - Backend:    http://localhost:8080"
echo "  - Health:     http://localhost:8080/actuator/health"
echo "  - Nginx:      http://localhost"
echo "  - Prometheus: http://localhost:9090"
echo "  - Grafana:    http://localhost:3000"
echo ""
echo "📋 Para verificar logs:"
echo "  docker-compose logs -f backend"
echo "  docker-compose logs -f mysql"
echo ""

