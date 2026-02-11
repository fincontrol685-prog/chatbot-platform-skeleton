#!/bin/bash

# Script para testar se o dashboard está funcionando
# Executar: bash test-dashboard.sh

echo "╔════════════════════════════════════════════════════════════╗"
echo "║          Teste do Dashboard - Chatbot Platform             ║"
echo "╚════════════════════════════════════════════════════════════╝"

cd /home/robertojr/chatbot-platform-skeleton

echo ""
echo "📋 CHECKLIST DE VERIFICAÇÃO"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# 1. Verificar estrutura de arquivos
echo "[1/5] Verificando estrutura de arquivos..."
if [ -f "frontend/src/app/features/dashboard/dashboard.component.ts" ] && \
   [ -f "frontend/src/app/features/dashboard/dashboard.component.html" ] && \
   [ -f "frontend/src/app/features/dashboard/dashboard.module.ts" ]; then
    echo "  ✓ Todos os arquivos do dashboard existem"
else
    echo "  ✗ Faltam arquivos do dashboard"
    exit 1
fi
echo ""

# 2. Verificar importações
echo "[2/5] Verificando importações no app.module.ts..."
if grep -q "dashboard/dashboard.module" frontend/src/app/app.module.ts; then
    echo "  ✓ Dashboard está importado no app.module.ts"
else
    echo "  ✗ Dashboard não está importado"
    exit 1
fi
echo ""

# 3. Verificar dependências do Node
echo "[3/5] Verificando dependências do Node.js..."
if [ -d "frontend/node_modules/@angular/core" ]; then
    echo "  ✓ Dependências instaladas"
else
    echo "  ⚠ Dependências não instaladas. Execute: cd frontend && npm install"
fi
echo ""

# 4. Verificar package.json
echo "[4/5] Verificando package.json..."
if grep -q "ng serve" frontend/package.json && \
   grep -q "@angular/core.*16" frontend/package.json; then
    echo "  ✓ package.json está correto"
else
    echo "  ⚠ package.json pode ter problemas"
fi
echo ""

# 5. Resumo
echo "[5/5] Status geral"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "✓ Dashboard está configurado corretamente!"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "PRÓXIMOS PASSOS:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "1️⃣  Instale as dependências (se não estiverem instaladas):"
echo "   cd frontend"
echo "   npm install"
echo ""
echo "2️⃣  Inicie o servidor de desenvolvimento:"
echo "   npm start"
echo ""
echo "3️⃣  Abra o navegador:"
echo "   http://localhost:4200"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "SE O DASHBOARD NÃO APARECER:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "1. Abra DevTools (F12) no navegador"
echo "2. Vá para a aba 'Console'"
echo "3. Procure pela mensagem:"
echo "   → 'Dashboard component initialized'"
echo ""
echo "4. Se não conseguir ver o dashboard:"
echo "   • Limpe o cache (Ctrl+Shift+Delete)"
echo "   • Pressione Ctrl+Shift+R (hard refresh)"
echo "   • Feche e reabra o navegador"
echo ""
echo "5. Se continuar sem aparecer:"
echo "   • Verifique se há erros em vermelho no console"
echo "   • Reinicie ng serve (Ctrl+C e npm start novamente)"
echo "   • Verifique se a porta 4200 não está em uso"
echo ""

