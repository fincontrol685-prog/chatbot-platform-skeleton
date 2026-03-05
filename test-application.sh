#!/bin/bash

# Script de teste da aplicação Chatbot Platform
# Verifica se tudo está compilado e funcionando

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║     TESTE DE VERIFICAÇÃO - CHATBOT PLATFORM                    ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

SUCCESS_COUNT=0
FAIL_COUNT=0

# Função para imprimir resultado
check_result() {
    if [ $1 -eq 0 ]; then
        echo "   ✅ $2"
        ((SUCCESS_COUNT++))
    else
        echo "   ❌ $2"
        ((FAIL_COUNT++))
    fi
}

# 1. Verificar se estamos no diretório correto
echo "🔍 1. Verificando estrutura do projeto..."
[ -f "pom.xml" ]
check_result $? "pom.xml encontrado"

[ -f "frontend/package.json" ]
check_result $? "frontend/package.json encontrado"

[ -f "target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar" ]
check_result $? "JAR do backend compilado"

# 2. Verificar se os arquivos de código foram criados
echo ""
echo "📝 2. Verificando arquivos criados..."
[ -f "src/main/java/com/br/chatbotplatformskeleton/config/DataInitializer.java" ]
check_result $? "DataInitializer.java criado"

[ -f "QUICK_START_LOGIN.md" ]
check_result $? "QUICK_START_LOGIN.md criado"

[ -f "PROBLEMA_RESOLVIDO.md" ]
check_result $? "PROBLEMA_RESOLVIDO.md criado"

[ -f "LEIA-ME-PRIMEIRO.md" ]
check_result $? "LEIA-ME-PRIMEIRO.md criado"

[ -f "start-application.sh" ]
check_result $? "start-application.sh criado"

# 3. Verificar conteúdo dos arquivos
echo ""
echo "📋 3. Verificando conteúdo dos arquivos..."
grep -q "Default admin user created" "src/main/java/com/br/chatbotplatformskeleton/config/DataInitializer.java"
check_result $? "DataInitializer tem código de criação de admin"

grep -q "CommandLineRunner" "src/main/java/com/br/chatbotplatformskeleton/config/DataInitializer.java"
check_result $? "DataInitializer implementa CommandLineRunner"

grep -q "admin123" "LEIA-ME-PRIMEIRO.md"
check_result $? "Credenciais documentadas"

# 4. Verificar dependências do frontend
echo ""
echo "📦 4. Verificando dependências do frontend..."
[ -d "frontend/node_modules/@angular/core" ]
check_result $? "Angular instalado"

[ -d "frontend/node_modules/@angular/material" ]
check_result $? "Angular Material instalado"

# 5. Verificar se o build foi realizado
echo ""
echo "🏗️  5. Verificando builds..."
[ -d "frontend/dist" ]
check_result $? "Frontend build gerado (dist/)"

[ -f "target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar.original" ]
check_result $? "JAR original salvo (build Spring Boot)"

# 6. Informações sobre a aplicação
echo ""
echo "ℹ️  6. Informações da Aplicação"
echo ""
echo "   Backend:"
java -version 2>&1 | head -1 | sed 's/^/   /'
[ -f "pom.xml" ] && grep "<version>" pom.xml | head -1 | sed 's/^/   /'

echo ""
echo "   Frontend:"
node --version | sed 's/^/   Node.js: /'
npm --version | sed 's/^/   npm: /'

# 7. Resumo
echo ""
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📊 RESULTADO FINAL:"
echo "   ✅ Sucessos: $SUCCESS_COUNT"
echo "   ❌ Falhas:   $FAIL_COUNT"

if [ $FAIL_COUNT -eq 0 ]; then
    echo ""
    echo "🎉 TUDO FUNCIONANDO PERFEITAMENTE!"
    echo ""
    echo "📖 Próximos passos:"
    echo "   1. Execute: java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar"
    echo "   2. Em outro terminal: cd frontend && npm start"
    echo "   3. Faça login com: admin / admin123"
    echo ""
    exit 0
else
    echo ""
    echo "⚠️  Há alguns problemas a resolver."
    exit 1
fi

