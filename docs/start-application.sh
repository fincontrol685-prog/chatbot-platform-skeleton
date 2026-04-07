#!/bin/bash

# Script para iniciar a aplicação Chatbot Platform com Backend e Frontend

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║        CHATBOT PLATFORM - QUICK START                          ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo "❌ Erro: Execute este script da raiz do projeto"
    echo "   cd /home/robertojr/chatbot-platform-skeleton"
    exit 1
fi

# Verificar se o JAR foi compilado
if [ ! -f "target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar" ]; then
    echo "❌ JAR não encontrado. Compilando o backend..."
    ./mvnw clean package -DskipTests -q
    if [ $? -ne 0 ]; then
        echo "❌ Erro ao compilar backend"
        exit 1
    fi
    echo "✅ Backend compilado com sucesso"
fi

echo ""
echo "🚀 Iniciando Backend Spring Boot..."
echo "   Porta: http://localhost:8080"
echo "   Log: /tmp/backend.log"
echo ""

# Matar qualquer processo Java anterior
pkill -f "java -jar.*chatbot-platform" 2>/dev/null || true
sleep 1

# Iniciar o backend em background
cd /home/robertojr/chatbot-platform-skeleton
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar > /tmp/backend.log 2>&1 &
BACKEND_PID=$!

echo "   PID: $BACKEND_PID"
sleep 8

# Verificar se o backend iniciou
if ! curl -s http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{}' > /dev/null 2>&1; then
    echo "❌ Erro: Backend não respondendo na porta 8080"
    echo "   Verifique o log: tail -50 /tmp/backend.log"
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
fi

echo "✅ Backend iniciado com sucesso!"
echo ""
echo "🌐 Iniciando Angular Frontend..."
echo "   Porta: http://localhost:4200"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "👤 CREDENCIAIS DE TESTE:"
echo "   Admin:"
echo "   ├─ Username: admin"
echo "   └─ Senha: admin123"
echo ""
echo "   Usuário:"
echo "   ├─ Username: user"
echo "   └─ Senha: user123"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""

# Iniciar o frontend
cd /home/robertojr/chatbot-platform-skeleton/frontend
ng serve --proxy-config proxy.conf.json --open

# Cleanup ao finalizar
echo ""
echo "⏹️  Encerrando aplicação..."
kill $BACKEND_PID 2>/dev/null || true
echo "✅ Aplicação encerrada"

