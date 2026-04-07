#!/bin/bash

# Chatbot Platform - Quick Setup Instructions
# =============================================

echo "🚀 Chatbot Platform - Docker Setup"
echo "===================================="
echo ""

# Step 1: Validate docker-compose
echo "✓ Validating docker-compose.yml..."
docker-compose config > /dev/null
echo "✓ Config is valid"
echo ""

# Step 2: Display next steps
echo "📋 Next Steps:"
echo ""
echo "1️⃣  First time setup:"
echo "    bash quick-start-prod.sh"
echo ""
echo "2️⃣  Or start services manually:"
echo "    cp .env.example .env"
echo "    docker-compose up -d"
echo ""
echo "3️⃣  Check status:"
echo "    docker-compose ps"
echo ""
echo "4️⃣  Check health:"
echo "    curl http://localhost:8080/actuator/health"
echo ""
echo "5️⃣  View logs:"
echo "    docker-compose logs -f backend"
echo ""
echo "6️⃣  Stop services:"
echo "    docker-compose down"
echo ""
echo "7️⃣  Remove everything (including data):"
echo "    docker-compose down -v"
echo ""

echo "✅ Docker Compose is ready!"

