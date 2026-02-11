#!/bin/bash

# Script para iniciar o Backend da Plataforma de Chatbots
# Executar este script para subir a aplicação Spring Boot

echo "================================"
echo "Iniciando Backend Spring Boot"
echo "================================"

# Mudar para o diretório raiz do projeto
cd /home/robertojr/chatbot-platform-skeleton

echo ""
echo "✓ Estou no diretório: $(pwd)"
echo ""

echo "✓ Iniciando servidor Spring Boot..."
echo "✓ A aplicação estará disponível em: http://localhost:8080"
echo "✓ Pressione Ctrl+C para parar o servidor"
echo ""

# Iniciar o servidor com Maven
./mvnw spring-boot:run

