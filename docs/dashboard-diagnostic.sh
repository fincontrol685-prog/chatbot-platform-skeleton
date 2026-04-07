#!/bin/bash

# Script para diagnosticar problemas com o dashboard

echo "╔════════════════════════════════════════════════════════════╗"
echo "║          Diagnóstico do Dashboard - Chatbot Platform       ║"
echo "╚════════════════════════════════════════════════════════════╝"

cd /home/robertojr/chatbot-platform-skeleton/frontend

echo ""
echo "[1] Verificando estrutura de arquivos do dashboard..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
ls -la src/app/features/dashboard/
echo ""

echo "[2] Verificando importações no app.module.ts..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
grep -A 3 "dashboard" src/app/app.module.ts
echo ""

echo "[3] Verificando conteúdo do dashboard.module.ts..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
cat src/app/features/dashboard/dashboard.module.ts
echo ""

echo "[4] Verificando conteúdo do dashboard.component.ts..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
cat src/app/features/dashboard/dashboard.component.ts
echo ""

echo "[5] Verificando dependencies..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
npm list @angular/core @angular/router @angular/material 2>/dev/null | head -10
echo ""

echo "✓ Diagnóstico concluído!"
echo ""
echo "Próximas etapas:"
echo "1. Execute: npm install"
echo "2. Execute: npm start"
echo "3. Acesse: http://localhost:4200"
echo ""

