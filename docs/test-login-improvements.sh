#!/bin/bash

# 🚀 Script Rápido para Testar a Página de Login Melhorada

echo "======================================"
echo "🎨 Testador de Página de Login"
echo "======================================"
echo ""

# Cores para terminal
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para verificar se comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Verificar Node.js
echo -e "${BLUE}📋 Verificando Pré-requisitos...${NC}"
echo ""

if command_exists node; then
    NODE_VERSION=$(node -v)
    echo -e "${GREEN}✅ Node.js ${NODE_VERSION} instalado${NC}"
else
    echo -e "${RED}❌ Node.js não encontrado. Instale em https://nodejs.org${NC}"
    exit 1
fi

if command_exists npm; then
    NPM_VERSION=$(npm -v)
    echo -e "${GREEN}✅ npm ${NPM_VERSION} instalado${NC}"
else
    echo -e "${RED}❌ npm não encontrado${NC}"
    exit 1
fi

# Verificar Angular CLI
if command_exists ng; then
    NG_VERSION=$(ng version 2>/dev/null | head -1)
    echo -e "${GREEN}✅ Angular CLI instalado${NC}"
else
    echo -e "${YELLOW}⚠️  Angular CLI não encontrado. Tentando instalar...${NC}"
    npm install -g @angular/cli
fi

echo ""
echo -e "${BLUE}📁 Navegando para pasta frontend...${NC}"
cd frontend || exit 1
echo -e "${GREEN}✅ Caminho: $(pwd)${NC}"

# Menu de opções
echo ""
echo -e "${BLUE}🎯 O que você deseja fazer?${NC}"
echo ""
echo "1) 📦 Instalar dependências"
echo "2) 🏗️  Compilar (production)"
echo "3) ⚡ Executar (desenvolvimento - ng serve)"
echo "4) 🧪 Rodar testes"
echo "5) ✨ Verificar erros de compilação"
echo "6) 🔍 Ver arquivos modificados"
echo "0) ❌ Sair"
echo ""
read -p "Escolha uma opção (0-6): " choice

case $choice in
    1)
        echo ""
        echo -e "${BLUE}📦 Instalando dependências...${NC}"
        npm install
        echo -e "${GREEN}✅ Dependências instaladas!${NC}"
        ;;
    2)
        echo ""
        echo -e "${BLUE}🏗️  Compilando para produção...${NC}"
        echo "Isto pode levar alguns minutos..."
        echo ""
        npm run build
        if [ $? -eq 0 ]; then
            echo ""
            echo -e "${GREEN}✅ Compilação concluída com sucesso!${NC}"
            echo -e "${BLUE}📁 Arquivos em: ./dist/chatbot-platform-skeleton${NC}"
        else
            echo -e "${RED}❌ Erro na compilação${NC}"
        fi
        ;;
    3)
        echo ""
        echo -e "${BLUE}⚡ Iniciando servidor de desenvolvimento...${NC}"
        echo ""
        echo -e "${YELLOW}📍 Acesse: http://localhost:4200${NC}"
        echo -e "${YELLOW}🔄 Será redirecionado automaticamente para /login${NC}"
        echo ""
        echo "Pressione Ctrl+C para parar o servidor"
        echo ""
        ng serve --open
        ;;
    4)
        echo ""
        echo -e "${BLUE}🧪 Rodando testes...${NC}"
        npm test
        ;;
    5)
        echo ""
        echo -e "${BLUE}✨ Verificando erros de compilação...${NC}"
        echo ""
        npx ng build --configuration development 2>&1 | grep -i error
        if [ $? -eq 0 ]; then
            echo -e "${RED}❌ Erros encontrados (veja acima)${NC}"
        else
            echo -e "${GREEN}✅ Nenhum erro de compilação!${NC}"
        fi
        ;;
    6)
        echo ""
        echo -e "${BLUE}🔍 Arquivos modificados para a página de login:${NC}"
        echo ""
        echo -e "${YELLOW}Criados:${NC}"
        echo "  📄 src/app/features/auth/login/login.component.css"
        echo ""
        echo -e "${YELLOW}Modificados:${NC}"
        echo "  📄 src/app/features/auth/login/login.component.html"
        echo "  📄 src/app/features/auth/login/login.component.ts"
        echo ""
        echo -e "${BLUE}📊 Tamanho dos arquivos:${NC}"
        echo ""
        if [ -f "src/app/features/auth/login/login.component.css" ]; then
            SIZE=$(wc -l < src/app/features/auth/login/login.component.css)
            echo "  login.component.css: $SIZE linhas"
        fi
        if [ -f "src/app/features/auth/login/login.component.html" ]; then
            SIZE=$(wc -l < src/app/features/auth/login/login.component.html)
            echo "  login.component.html: $SIZE linhas"
        fi
        if [ -f "src/app/features/auth/login/login.component.ts" ]; then
            SIZE=$(wc -l < src/app/features/auth/login/login.component.ts)
            echo "  login.component.ts: $SIZE linhas"
        fi
        ;;
    0)
        echo -e "${YELLOW}👋 Até logo!${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}❌ Opção inválida${NC}"
        ;;
esac

echo ""
echo "======================================"
echo "🎉 Processo concluído!"
echo "======================================"

