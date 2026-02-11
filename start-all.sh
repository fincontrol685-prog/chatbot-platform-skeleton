#!/bin/bash

# Script para iniciar Backend e Frontend simultaneamente
# Execute este script para subir toda a plataforma

echo "╔════════════════════════════════════════════════════════════╗"
echo "║   Plataforma de Chatbots - Iniciando Backend e Frontend    ║"
echo "╚════════════════════════════════════════════════════════════╝"

PROJECT_DIR="/home/robertojr/chatbot-platform-skeleton"

# Função para limpar ao sair
cleanup() {
    echo ""
    echo "╔════════════════════════════════════════════════════════════╗"
    echo "║   Encerrando servidores...                                  ║"
    echo "╚════════════════════════════════════════════════════════════╝"
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    exit 0
}

# Configurar trap para Ctrl+C
trap cleanup SIGINT

echo ""
echo "[1/2] Iniciando Backend (Spring Boot)..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Iniciar backend em background
cd "$PROJECT_DIR"
./mvnw spring-boot:run > /tmp/backend.log 2>&1 &
BACKEND_PID=$!
echo "✓ Backend iniciado (PID: $BACKEND_PID)"
echo "✓ URL: http://localhost:8080"
echo ""

# Aguardar um pouco para o backend iniciar
echo "⏳ Aguardando backend inicializar (15 segundos)..."
sleep 15

echo ""
echo "[2/2] Iniciando Frontend (Angular)..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Iniciar frontend em background
cd "$PROJECT_DIR/frontend"
npm run start > /tmp/frontend.log 2>&1 &
FRONTEND_PID=$!
echo "✓ Frontend iniciado (PID: $FRONTEND_PID)"
echo "✓ URL: http://localhost:4200"
echo ""

echo "╔════════════════════════════════════════════════════════════╗"
echo "║   ✓ Sistema pronto para uso!                               ║"
echo "║                                                             ║"
echo "║   Backend:  http://localhost:8080                          ║"
echo "║   Frontend: http://localhost:4200                          ║"
echo "║                                                             ║"
echo "║   Logs:                                                     ║"
echo "║   - Backend:  /tmp/backend.log                             ║"
echo "║   - Frontend: /tmp/frontend.log                            ║"
echo "║                                                             ║"
echo "║   Pressione Ctrl+C para parar os servidores                ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Aguardar indefinidamente
wait

