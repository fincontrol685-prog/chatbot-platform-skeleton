# ChunkLoadError Fix - Monitoramento e Auditoria

## Problema Identificado
O erro `ChunkLoadError: Loading chunk 75 failed` ocorria ao clicar no menu **"Monitoramento e auditoria"** porque o arquivo `index.html` não tinha a tag `<base href="/">` configurada.

**Erro Original:**
```
ChunkLoadError: Loading chunk 75 failed.
(error: http://localhost:4200/compliance/75.js)
```

A URL estava incorreta porque o navegador tentava resolver o chunk de forma relativa à rota atual (`/compliance/...`) ao invés de usar a raiz da aplicação.

## Solução Implementada

### 1. Adicionada tag `<base href="/">` ao index.html

A tag `<base>` especifica a URL base para todos URLs relativos em um documento HTML.

**Arquivo:** `frontend/src/index.html`

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <base href="/">  <!-- ← ADICIONADO -->
  <title>Chatbot Platform</title>
  ...
</head>
```

## Como Executar

### Desenvolvimento (ng serve)
```bash
cd frontend
npm install
npm start
```

O servidor rodará em `http://localhost:4200` e os chunks será carregados corretamente.

### Production (Build)
```bash
cd frontend
npm run build
```

Os arquivos compilados ficarão em `frontend/dist/frontend/`

## Verificação

Para confirmar que o problema foi resolvido:

1. **Desenvolvendo:** Execute `npm start`
2. **Login** na plataforma
3. **Clique no menu** "Monitoramento e auditoria"
4. **Abra o DevTools** (F12) → Console
5. **Verifique:** Não deve haver mais erros de ChunkLoadError

Os chunks agora serão carregados do caminho correto:
- ✅ Correto: `http://localhost:4200/75.js`
- ❌ Errado: `http://localhost:4200/compliance/75.js`

## Por Que Isso Ocorreu

O Angular utiliza **lazy loading** (carregamento sob demanda) para módulos grandes. Cada módulo é compilado em um chunk JavaScript separado que é carregado apenas quando necessário.

Sem a tag `<base>`, o navegador resolve URLs relativos baseado na rota atual:
- Se você está em `/compliance/security`, ele tenta carregar `/compliance/75.js`
- Com a tag `<base href="/">`, ele carrega `/75.js` (a partir da raiz)

## Modificações Realizadas

- ✅ `frontend/src/index.html` - Adicionada tag `<base href="/>`
- ✅ `frontend/dist/` - Rebuild na sequência (chunks gerados corretamente)

## Referencias

- [Angular Base Href Documentation](https://angular.io/guide/router#base-href)
- [Angular Lazy Loading Module Docs](https://angular.io/guide/router#lazy-loading-route-configuration)

