# 🛠️ Guia Técnico de Remediação de CVEs

**Chatbot Platform Skeleton - Instruções Passo-a-Passo para Atualização**

---

## 📋 Índice

1. [Pré-requisitos](#pré-requisitos)
2. [Backup](#backup)
3. [Atualização de Dependências](#atualização-de-dependências)
4. [Resolução de Conflitos](#resolução-de-conflitos)
5. [Testes de Validação](#testes-de-validação)
6. [Troubleshooting](#troubleshooting)

---

## Pré-requisitos

### Verificar Ambiente

```bash
# Verificar versão do Node.js
node --version
# Recomendado: Node 18+

# Verificar versão do npm
npm --version
# Recomendado: npm 9+

# Verificar instalação do Angular CLI global
ng version
# Se não tiver, instale: npm install -g @angular/cli@21
```

### Verificar Status Git

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Garantir que git está limpo
git status
# Output esperado: "working tree clean"

# Se houver mudanças não commitadas:
git add .
git commit -m "Pre-CVE remediation checkpoint"
```

---

## Backup

### Passo 1: Criar Backup dos Arquivos Críticos

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Backup de package.json
cp package.json package.json.backup-2026-03-12

# Backup de package-lock.json
cp package-lock.json package-lock.json.backup-2026-03-12

# Verificar backups
ls -la package*.backup*
```

### Passo 2: Criar Branch Git para Atualização

```bash
cd /home/robertojr/chatbot-platform-skeleton

# Criar branch específico para CVE fixes
git checkout -b fix/cve-angular-21-upgrade-2026-03

# Verificar branch
git branch -v
```

---

## Atualização de Dependências

### Opção A: Atualização Manual (Recomendado para Controle)

#### 1. Editar `frontend/package.json`

Abra `/home/robertojr/chatbot-platform-skeleton/frontend/package.json` e atualize as versões:

**ANTES:**
```json
{
  "dependencies": {
    "@angular/animations": "16.2.0",
    "@angular/cdk": "16.2.0",
    "@angular/common": "16.2.0",
    "@angular/compiler": "16.2.0",
    "@angular/core": "16.2.0",
    "@angular/forms": "16.2.0",
    "@angular/material": "16.2.0",
    "@angular/platform-browser": "16.2.0",
    "@angular/platform-browser-dynamic": "16.2.0",
    "@angular/router": "16.2.0",
    "rxjs": "7.8.1",
    "zone.js": "0.12.0"
  },
  "devDependencies": {
    "@angular-devkit/build-angular": "16.2.0",
    "@angular/cli": "16.2.0",
    "@angular/compiler-cli": "16.2.0",
    "@types/jasmine": "4.3.1",
    "@types/node": "18.16.18",
    "angular-eslint": "21.0.1",
    "eslint": "9.39.1",
    "jasmine-core": "5.1.0",
    "karma": "6.4.1",
    "karma-chrome-launcher": "3.2.0",
    "karma-jasmine": "5.1.0",
    "typescript": "5.1.6",
    "typescript-eslint": "8.46.4"
  }
}
```

**DEPOIS:**
```json
{
  "dependencies": {
    "@angular/animations": "^21.2.0",
    "@angular/cdk": "^21.2.0",
    "@angular/common": "^21.2.0",
    "@angular/compiler": "^21.2.0",
    "@angular/core": "^21.2.0",
    "@angular/forms": "^21.2.0",
    "@angular/material": "^21.2.0",
    "@angular/platform-browser": "^21.2.0",
    "@angular/platform-browser-dynamic": "^21.2.0",
    "@angular/router": "^21.2.0",
    "rxjs": "^7.8.1",
    "zone.js": "^0.14.0"
  },
  "devDependencies": {
    "@angular-devkit/build-angular": "^21.2.0",
    "@angular/cli": "^21.2.0",
    "@angular/compiler-cli": "^21.2.0",
    "@types/jasmine": "~5.1.0",
    "@types/node": "^20.0.0",
    "angular-eslint": "^16.0.0",
    "eslint": "^9.0.0",
    "jasmine-core": "^5.1.0",
    "karma": "^6.4.0",
    "karma-chrome-launcher": "^3.2.0",
    "karma-jasmine": "^5.1.0",
    "typescript": "^5.2.0",
    "typescript-eslint": "^8.0.0"
  }
}
```

#### 2. Limpar Cache do npm

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Limpar cache
npm cache clean --force

# Remover node_modules e lockfiles para reinstalação fresca
rm -rf node_modules
rm package-lock.json
```

#### 3. Instalar Novas Dependências

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Instalar com novo package-lock
npm install

# Isso pode levar 5-10 minutos
# Observe por mensagens de erro durante a instalação
```

### Opção B: Atualização Interativa (npm update)

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Atualizar para versões maiores (requer versões atualizadas no package.json)
npm update

# Verificar versões instaladas
npm list @angular/core
npm list @angular/compiler
npm list @angular/common
```

---

## Resolução de Conflitos

### Se houver conflitos de peer dependencies:

```bash
# Instalar com força (não recomendado como primeira opção)
npm install --force

# OU: Tentar resolver automaticamente
npm install --legacy-peer-deps
```

### Se houver erro de TypeScript incompatibilidade:

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Verificar versão instalada
npm ls typescript

# Se necessário, atualizar tsconfig.json:
# Alterar "target" para ES2020 ou superior
# Alterar "lib" para incluir ES2020 ou superior
```

### Se houver erro de Angular i18n:

```bash
# Limpar cache de build
ng cache clean

# Reconstruir
ng build
```

---

## Testes de Validação

### 1. Verificar Instalação

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Listar versões dos pacotes principais
npm ls @angular/core @angular/compiler @angular/common

# Output esperado:
# @angular/core@21.x.x
# @angular/compiler@21.x.x
# @angular/common@21.x.x
```

### 2. Build de Produção

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Build para produção
npm run build

# Expected output:
# ✔ Compiled successfully
# ✔ Build at: dist/frontend/...
```

### 3. Executar Testes

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Executar testes unitários
npm test

# Executar linter
npm run lint

# Ambos devem terminar sem erros críticos
```

### 4. Validar CVEs Novamente

```bash
# Após atualização bem-sucedida, re-validar:
# (Usar ferramenta de verificação de CVEs do seu sistema)

# No contexto deste projeto, você executaria:
# npm audit

npm audit
# Esperado: No vulnerabilities found (ou apenas LOW severity)
```

### 5. Verificação de Compatibilidade

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Testar compilação Angular
ng build --configuration development

# Testar server de desenvolvimento
ng serve
# Acessar http://localhost:4200 no navegador
```

---

## Testes Funcionais

### Teste Manual no Navegador

1. **Abra o aplicativo em http://localhost:4200**

2. **Teste Login:**
   - Vá para página de login
   - Insira credenciais válidas
   - Verifique se login funciona

3. **Teste Dashboard:**
   - Após login, verificar se dashboard carrega
   - Teste navigação entre páginas

4. **Verifique Console do Navegador:**
   - Abra DevTools (F12)
   - Vá para aba "Console"
   - Não deve haver erros vermelhos

5. **Verifique Network:**
   - Na aba "Network" do DevTools
   - Verifique se requisições para backend (API) funcionam
   - Status esperado: 200 OK

---

## Troubleshooting

### Erro: "Module not found"

```bash
# Solução:
cd /home/robertojr/chatbot-platform-skeleton/frontend
rm -rf node_modules
npm cache clean --force
npm install
```

### Erro: "Incompatible peer dependencies"

```bash
# Se npm install falhar:
npm install --legacy-peer-deps

# NOTA: Isso não é ideal, mas funciona para compatibilidade
```

### Erro: TypeScript compilation errors

```bash
# Verificar se há erros de tipo
ng build --configuration development

# Se houver erros, revise:
# 1. tsconfig.json
# 2. Arquivo de erro específico mencionado
# 3. Atualizações de API do Angular 21
```

### Erro: "Cannot find name 'X'" durante build

```bash
# Isso geralmente significa que uma importação mudou no Angular 21
# Verificar imports em arquivos .ts:

# Exemplo de import antigo (Angular 16):
# import { X } from '@angular/common';

# Pode ter mudado para (Angular 21):
# import { X } from '@angular/common/http';

# Use find e replace para atualizar
```

### Aplicação não funciona após update

```bash
# Reverter para versão anterior:
git checkout package.json.backup-2026-03-12
git checkout package-lock.json.backup-2026-03-12
rm -rf node_modules
npm install

# Depois:
# 1. Revisar breaking changes do Angular 21
# 2. Corrigir código conforme necessário
# 3. Tentar update novamente
```

---

## Comandos de Referência Rápida

```bash
# Navegar para frontend
cd /home/robertojr/chatbot-platform-skeleton/frontend

# Ver todas as dependências
npm ls

# Ver dependências desatualizadas
npm outdated

# Atualizar todas as dependências para versão minor/patch
npm update

# Instalar versão específica de um pacote
npm install @angular/core@21.2.0

# Desinstalar um pacote
npm uninstall @angular/core

# Limpar cache
npm cache clean --force

# Ver histórico de versões de um pacote
npm view @angular/core versions

# Atualizar npm itself
npm install -g npm@latest

# Executar build em modo desenvolvimento
npm run build -- --configuration development

# Executar build em modo produção
npm run build -- --configuration production

# Executar server de desenvolvimento com watch
npm start

# Executar testes em watch mode
npm test -- --watch
```

---

## Versionamento Semântico

Referência rápida de versionamento npm:

- `^21.2.0` = Permite atualizações menores e patches (21.3.0, 21.4.5 mas não 22.0.0)
- `~21.2.0` = Permite apenas atualizações de patches (21.2.5 mas não 21.3.0)
- `21.2.0` = Versão exata (nenhuma atualização automática)
- `*` = Qualquer versão (não recomendado)

**Recomendação:** Use `^` para maior flexibilidade com patches e updates menores.

---

## Próximos Passos após Sucesso

1. ✅ Commit das mudanças
   ```bash
   git add .
   git commit -m "chore: upgrade Angular from 16 to 21 (fix CVEs)"
   ```

2. ✅ Push para branch de feature
   ```bash
   git push -u origin fix/cve-angular-21-upgrade-2026-03
   ```

3. ✅ Criar Pull Request no GitHub/GitLab

4. ✅ Executar CI/CD pipeline

5. ✅ Fazer code review

6. ✅ Merge na branch main/develop

7. ✅ Deploy em staging

8. ✅ Testes de regressão em staging

9. ✅ Deploy em produção

---

## Recursos Adicionais

- [Angular 21 Release Notes](https://github.com/angular/angular/releases/tag/21.0.0)
- [Angular Migration Guide 16 → 21](https://angular.io/guide/upgrade)
- [npm Documentation](https://docs.npmjs.com/)
- [TypeScript 5.2 Release Notes](https://www.typescriptlang.org/docs/handbook/release-notes/typescript-5-2.html)

---

**Última atualização:** 12 de Março de 2026

