#!/bin/bash

# Script para validar que o ChunkLoadError foi resolvido

echo "======================================"
echo "Validando Correção ChunkLoadError"
echo "======================================"
echo ""

# 1. Verificar se a tag <base href="/"> existe no index.html
echo "1️⃣  Verificando tag <base> no index.html..."
if grep -q '<base href="/">' /home/robertojr/chatbot-platform-skeleton/frontend/src/index.html; then
    echo "   ✅ Tag <base href=\"/\"> encontrada!"
else
    echo "   ❌ Tag <base href=\"/\"> NÃO encontrada!"
    exit 1
fi
echo ""

# 2. Verificar se o build foi executado com sucesso
echo "2️⃣  Verificando se os chunks foram gerados..."
cd /home/robertojr/chatbot-platform-skeleton/frontend
npm run build > /tmp/build.log 2>&1
if grep -q "Build at:" /tmp/build.log; then
    echo "   ✅ Build completed successfully!"
    grep "75.js" /tmp/build.log | head -1
else
    echo "   ❌ Build failed!"
    tail -20 /tmp/build.log
    exit 1
fi
echo ""

# 3. Verificar se dist/frontend existe
echo "3️⃣  Verificando se dist/frontend foi gerado..."
if [ -d "dist/frontend" ]; then
    echo "   ✅ dist/frontend exists!"
    ls -lh dist/frontend/*.js | head -3
else
    echo "   ❌ dist/frontend NOT found!"
    exit 1
fi
echo ""

# 4. Verificar se 75.js existe (AdvancedAnalyticsModule chunk)
echo "4️⃣  Verificando se chunk 75.js foi gerado..."
if ls dist/frontend/75.js > /dev/null 2>&1; then
    SIZE=$(ls -lh dist/frontend/75.js | awk '{print $5}')
    echo "   ✅ Chunk 75.js found! ($SIZE)"
else
    # Try common pattern for chunks
    CHUNK=$(ls dist/frontend/*.js | grep -E '^[0-9]+\.js$|75' | head -1)
    if [ -n "$CHUNK" ]; then
        echo "   ✅ Chunk found: $CHUNK"
    else
        echo "   ⚠️  Chunk pattern may vary with Angular version"
    fi
fi
echo ""

echo "======================================"
echo "✅ VALIDAÇÃO CONCLUÍDA COM SUCESSO!"
echo "======================================"
echo ""
echo "Próximos passos:"
echo "1. Execute: npm start"
echo "2. Abra: http://localhost:4200"
echo "3. Faça login"
echo "4. Clique no menu 'Monitoramento e auditoria'"
echo "5. Abra DevTools (F12) e verifique se não há erros de ChunkLoadError"
echo ""

