# ✅ Checklist de Testes - Tela de Login

## 🧪 Testes Unitários

### LoginComponent
- [ ] Formulário é criado com campos username e password
- [ ] Validação requer username não vazio
- [ ] Validação requer password não vazio
- [ ] Botão Submit fica desabilitado com formulário inválido
- [ ] Ao submeter com dados válidos, carregamento é ativado
- [ ] Ao receber sucesso, usuário é redirecionado para /bots
- [ ] Ao receber erro, mensagem de erro é exibida
- [ ] Flag loading desativa o botão e mostra loading bar

### AuthService
- [ ] login() faz POST request para /api/auth/login
- [ ] login() armazena token em localStorage com chave 'access_token'
- [ ] logout() remove token de localStorage
- [ ] getToken() retorna token ou null
- [ ] isAuthenticated() retorna true se token existe

### AuthGuard
- [ ] canActivate() retorna true se autenticado
- [ ] canActivate() redireciona para /login se não autenticado

### AppComponent
- [ ] Navbar mostra Login quando não autenticado
- [ ] Navbar mostra Logout quando autenticado
- [ ] Logout redireciona para /login
- [ ] Links de navegação são mostrados apenas se autenticado

## 🌐 Testes de Integração

### Fluxo de Login
- [ ] Acessar http://localhost:4200 redireciona para /login
- [ ] Página de login carrega sem erros
- [ ] Form está visível e funcional
- [ ] Inputs aceitam texto
- [ ] Validação mostra erro quando campo vazio
- [ ] Submeter com username/password válido:
  - [ ] Loading bar aparece
  - [ ] Botão fica desabilitado
  - [ ] Requisição é enviada para backend
  - [ ] Token é armazenado em localStorage
  - [ ] Usuário redireciona para /bots
  - [ ] Token aparece em Authorization header em próximas requisições

### Fluxo de Erro
- [ ] Submeter com username/password inválido:
  - [ ] Loading bar desaparece
  - [ ] Botão volta a estar habilitado
  - [ ] Mensagem de erro é exibida
  - [ ] Token NÃO é armazenado
  - [ ] Usuário permanece na página de login

### Logout
- [ ] Clicar em Logout remove token
- [ ] Usuário redireciona para /login
- [ ] Próximas requisições não incluem token
- [ ] Rota protegida redireciona para /login

## 🎨 Testes de UI/UX

### Visual
- [ ] Página tem gradiente de fundo bonito
- [ ] Card de login está centralizado e bem proporcionado
- [ ] Título "Chatbot Platform" aparece
- [ ] Inputs têm placeholder descritivo
- [ ] Erros aparecem em vermelho com fundo destacado
- [ ] Loading bar tem cor apropriada
- [ ] Layout é responsivo em mobile

### Interatividade
- [ ] Clicar em input ativa o campo
- [ ] Tab key navega entre campos
- [ ] Enter submete o formulário
- [ ] Botão tem hover effect
- [ ] Cursor muda para pointer em botão
- [ ] Loading desabilita inputs

### Acessibilidade
- [ ] Labels associados aos inputs
- [ ] Validação mostra mat-error
- [ ] Tecla Tab navega logicamente
- [ ] Cores têm contraste suficiente
- [ ] Mensagens de erro são acessíveis

## 🔐 Testes de Segurança

### Autenticação
- [ ] Senha não é mostrada em texto plano
- [ ] Token é armazenado com chave apropriada
- [ ] Token é enviado apenas em Authorization header
- [ ] XSS: Entrada do usuário não é interpretada como código
- [ ] CSRF: Requisição POST tem proteção apropriada

### Backend
- [ ] /api/auth/login aceita POST
- [ ] /api/auth/login rejeita GET, PUT, DELETE
- [ ] Senha é comparada com hash, não em texto plano
- [ ] JWT é validado nas próximas requisições
- [ ] Routes protegidas retornam 401 sem token

## 📊 Testes de Performance

### Carregamento
- [ ] Página de login carrega em < 2 segundos
- [ ] Bundle de auth module é < 10 kB
- [ ] Não há vazamento de memória ao deslogar/logar várias vezes

### Requisições
- [ ] Login request < 500ms
- [ ] Múltiplos logins em sequência funcionam
- [ ] Cache do browser não interfere no logout

## 🔄 Testes de Compatibilidade

### Navegadores
- [ ] Chrome/Chromium (testado)
- [ ] Firefox (testado)
- [ ] Safari (testado)
- [ ] Edge (testado)

### Dispositivos
- [ ] Desktop (1920x1080)
- [ ] Tablet (768x1024)
- [ ] Mobile (375x667)

## 📱 Testes de Browser DevTools

### Console
- [ ] Sem erros (console.error)
- [ ] Sem warnings críticos
- [ ] Login error detalhado aparece no console

### Network
- [ ] Request para /api/auth/login é POST
- [ ] Content-Type: application/json
- [ ] Response status 200 com token
- [ ] Response status 401 com credenciais inválidas

### Storage
- [ ] access_token aparece em localStorage após login
- [ ] access_token desaparece após logout
- [ ] localStorage tem apenas o token necessário

## 🧪 Testes Manuais

### Cenário 1: Login Válido
```
1. Acessar http://localhost:4200
2. Preencher username: "admin"
3. Preencher password: "admin123"
4. Clicar "Entrar"
5. ✅ Esperado: Redirecionado para /bots com token armazenado
```

### Cenário 2: Login Inválido
```
1. Acessar http://localhost:4200/login
2. Preencher username: "admin"
3. Preencher password: "wrong"
4. Clicar "Entrar"
5. ✅ Esperado: Mensagem de erro vermelha aparece, permanece em /login
```

### Cenário 3: Campos Vazios
```
1. Acessar http://localhost:4200/login
2. Não preencher nada
3. Clicar "Entrar"
4. ✅ Esperado: Botão desabilitado, erros mostrados abaixo dos campos
```

### Cenário 4: Logout
```
1. Estar autenticado (na página de /bots)
2. Clicar "Logout" no canto superior direito
3. ✅ Esperado: Redirecionado para /login, token removido
```

### Cenário 5: Rota Protegida
```
1. Fazer logout (deletar token do localStorage manualmente se necessário)
2. Tentar acessar http://localhost:4200/bots
3. ✅ Esperado: Redirecionado para /login automaticamente
```

## 📋 Registro de Testes

### Primeiro Teste (Data: _________)
- Testador: _________________
- Navegador: _________________
- Resultado: ✅ PASSOU / ❌ FALHOU
- Notas: _________________________________

### Segundo Teste (Data: _________)
- Testador: _________________
- Navegador: _________________
- Resultado: ✅ PASSOU / ❌ FALHOU
- Notas: _________________________________

## 🐛 Bugs Encontrados

| ID | Descrição | Severidade | Status | Data |
|----|-----------|-----------|--------|------|
| | | | | |

## ✅ Approval

- [ ] Frontend (Login UI) aprovado
- [ ] Backend (Auth API) aprovado
- [ ] Integração aprovada
- [ ] Deploy aprovado

**Data de Aprovação:** ____________
**Assinado por:** _________________

---

## 📞 Suporte

Se encontrar problemas durante os testes:

1. Consultar `STARTUP_GUIDE.md`
2. Consultar `TECHNICAL_SUMMARY.md`
3. Verificar logs do backend: `/tmp/backend.log`
4. Verificar console do navegador (F12)
5. Verificar Network tab para requisições HTTP

