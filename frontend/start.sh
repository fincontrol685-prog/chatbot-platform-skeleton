#!/bin/bash

# Script para iniciar o Frontend da Plataforma de Chatbots
# Executar este script para subir o Angular Dev Server

echo "================================"
echo "Iniciando Frontend Angular"
echo "================================"

# Mudar para o diretório do frontend
cd /home/robertojr/chatbot-platform-skeleton/frontend

echo ""
echo "✓ Estou no diretório: $(pwd)"
echo ""

# Verificar se node_modules existe
if [ ! -d "node_modules" ]; then
    echo "⚠ node_modules não encontrado. Instalando dependências..."
    npm install
fi

echo ""
echo "✓ Iniciando servidor Angular..."
echo "✓ Acesse a aplicação em: http://localhost:4200"
echo "✓ Pressione Ctrl+C para parar o servidor"
echo ""

# Iniciar o servidor com proxy configurado
npm run start

