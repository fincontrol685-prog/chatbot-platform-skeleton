# 🚀 GUIA RÁPIDO - TESTE DO FRONTEND ATUALIZADO

**Tempo estimado:** 5-10 minutos

---

## ✅ PRÉ-REQUISITOS

1. **Backend rodando:**
   ```bash
   cd /home/robertojr/chatbot-platform-skeleton
   ./mvnw spring-boot:run
   # Backend em: http://localhost:8080
   ```

2. **Frontend em desenvolvimento:**
   ```bash
   cd /home/robertojr/chatbot-platform-skeleton/frontend
   npm install
   npm start
   # Frontend em: http://localhost:4200
   ```

3. **Logado no sistema** com um usuário válido

---

## 🧪 TESTE 1: GERENCIAMENTO PROFISSIONAL

### 1.1 Criar Departamento
- [ ] Acesse: `http://localhost:4200/professional/departments`
- [ ] Clique em "Novo Departamento"
- [ ] Preencha:
  - Nome: `TI`
  - Código: `IT`
  - Localização: `São Paulo`
  - Descrição: `Departamento de Tecnologia`
- [ ] Clique em "Criar"
- [ ] Verá mensagem de sucesso
- [ ] Departamento aparecerá na lista

### 1.2 Criar Equipe
- [ ] Acesse: `http://localhost:4200/professional/teams`
- [ ] Clique em "Nova Equipe"
- [ ] Preencha:
  - Nome: `Backend Team`
  - Código: `BACKEND`
  - Departamento: `TI` (selecione o que criou acima)
  - Máximo de Conversas: `15`
- [ ] Clique em "Criar"
- [ ] Equipe será adicionada à lista

### 1.3 Buscar Departamento
- [ ] Na lista de departamentos, digite `TI` no campo de busca
- [ ] A lista será filtrada
- [ ] Clique em um departamento para ver detalhes

**✅ Resultado esperado:** Departamentos e equipes criadas com sucesso

---

## 🔐 TESTE 2: 2FA (AUTENTICAÇÃO EM DUAS ETAPAS)

### 2.1 Setup Inicial 2FA
- [ ] Acesse: `http://localhost:4200/compliance/security`
- [ ] Veja o status atual (deve estar "Desativado")
- [ ] Clique em "Ativar 2FA"

### 2.2 Escanear Código QR
- [ ] Um código QR será exibido
- [ ] Abra seu telefone com **Google Authenticator** ou **Authy**
- [ ] Escaneie o código QR
- [ ] Você verá um código de 6 dígitos começando a contar
- [ ] Copie esse código

### 2.3 Verificar Código
- [ ] Volte para o navegador
- [ ] Insira o código de 6 dígitos no campo "Código do Autenticador"
- [ ] Clique em "Ativar"

### 2.4 Guardar Backup Codes
- [ ] Você receberá 10 "Códigos de Backup"
- [ ] ⚠️ **IMPORTANTE:** Salve esses códigos em local seguro
- [ ] Use-os se perder acesso ao authenticador
- [ ] Clique em OK

### 2.5 Desativar 2FA (Opcional)
- [ ] Volte à tela de status
- [ ] Clique em "Desativar 2FA"
- [ ] Confirme
- [ ] Status deve voltar a "Desativado"

**✅ Resultado esperado:** 2FA ativado com sucesso

---

## 📋 TESTE 3: CONSENTIMENTO GDPR

### 3.1 Ver Consentimentos
- [ ] Acesse: `http://localhost:4200/compliance/consent`
- [ ] Veja lista de tipos de consentimento:
  - Marketing e Promoções
  - Analytics
  - Dados Pessoais
  - GDPR

### 3.2 Conceder Consentimento
- [ ] Marque a checkbox de "Marketing e Promoções"
- [ ] Sistema enviará POST `/api/compliance/consent`
- [ ] Verá mensagem "Consentimento concedido"

### 3.3 Revogar Consentimento
- [ ] Desmarque "Marketing e Promoções"
- [ ] Confirme a revogação
- [ ] Mensagem "Consentimento revogado"
- [ ] Item será removido da lista

**✅ Resultado esperado:** Consentimentos gerenciados com sucesso

---

## 📊 TESTE 4: ANALYTICS AVANÇADO

### 4.1 Acessar Dashboard
- [ ] Acesse: `http://localhost:4200/analytics-advanced/dashboard`
- [ ] Veja os filtros:
  - Tipo de filtro: Bot, Equipe ou Departamento
  - ID (número)
  - Data inicial e final (opcionais)

### 4.2 Buscar Métricas
- [ ] Selecione: "Bot"
- [ ] Insira ID: `1` (ou ID de um bot que exista)
- [ ] Clique em "Buscar"
- [ ] Se houver métricas, elas aparecerão em tabela

### 4.3 Exportar Excel
- [ ] Com métricas carregadas, clique em "Exportar Excel"
- [ ] Arquivo `.xlsx` será baixado
- [ ] Abra em Excel/LibreOffice

### 4.4 Exportar CSV
- [ ] Clique em "Exportar CSV"
- [ ] Arquivo `.csv` será baixado
- [ ] Abra em planilha

**✅ Resultado esperado:** Métricas exibidas e exportadas com sucesso

---

## 📝 TESTE 5: RELATÓRIOS CUSTOMIZADOS

### 5.1 Acessar Relatórios
- [ ] Acesse: `http://localhost:4200/analytics-advanced/reports`
- [ ] Veja "Meus Relatórios" (inicialmente vazio)

### 5.2 Criar Novo Relatório
- [ ] Clique em "Novo Relatório"
- [ ] Preencha:
  - Nome: `Relatório de Conversas Semana`
  - Descrição: `Métricas de conversas da última semana`
  - Tipo: `CHART`
  - Tipo de Gráfico: `LINE`
  - Agrupar por: `date`
  - Métricas: Selecione `CONVERSATION_COUNT` e `RESPONSE_TIME`
  - Compartilhamento: deixe privado
- [ ] Clique em "Criar"
- [ ] Relatório aparecerá na lista

### 5.3 Visualizar Relatório
- [ ] Clique no relatório criado
- [ ] Veja detalhes: nome, tipo, métricas, etc.

### 5.4 Visualizar Todos Acessíveis
- [ ] Clique em "Todos Acessíveis"
- [ ] Sistema carrega relatórios públicos de outros usuários
- [ ] Volte a "Meus Relatórios"

**✅ Resultado esperado:** Relatórios criados e visualizados com sucesso

---

## 🔍 VERIFICAÇÃO FINAL

### Verificar no Console do Navegador (F12)
- [ ] Abra DevTools: `F12`
- [ ] Vá para aba "Network"
- [ ] Faça alguma ação (criar, buscar, exportar)
- [ ] Verá requests para API:
  - `POST /api/departments` 
  - `GET /api/teams`
  - `POST /api/compliance/consent`
  - `POST /api/analytics-advanced/export/excel`
  - etc.
- [ ] Todos devem retornar status `200` (sucesso)

### Verificar Tokens JWT
- [ ] Abra DevTools: `F12`
- [ ] Vá para aba "Application" → "Local Storage"
- [ ] Procure por `token` ou `jwt`
- [ ] Token deve estar armazenado
- [ ] Não limpe sem fazer logout antes!

---

## ❌ TROUBLESHOOTING

### Erro: "Erro ao carregar departamentos"
```
Cause: Backend não rodando ou autenticação falhou
Fix: 
  1. Verifique se backend está em http://localhost:8080
  2. Faça logout e login novamente
  3. Veja console (F12) para mais detalhes
```

### Erro: "CORS error"
```
Cause: Configuração de CORS no backend
Fix:
  1. Verifique application.yml do backend
  2. Adicione frontend URL ao CORS allowed origins
  3. Restart backend
```

### Erro: "Código TOTP inválido"
```
Cause: Relógio do servidor/cliente dessincronizado
Fix:
  1. Verifique data/hora do sistema
  2. Sincronize relógio
  3. Tente novamente
```

### 404 em exports/downloads
```
Cause: Endpoint de export não implementado no backend
Fix:
  1. Verifique se AdvancedAnalyticsController tem /export/excel
  2. Verifique se arquivo foi gerado no servidor
  3. Tente em formato diferente (CSV em vez de Excel)
```

---

## 📞 SUPORTE

Se encontrar problemas:

1. **Verifique os logs:**
   ```bash
   # Terminal do backend
   tail -f backend.log
   
   # Console do navegador (F12)
   Ctrl+Shift+J
   ```

2. **Verifique conectividade:**
   ```bash
   curl http://localhost:8080/api/departments \
     -H "Authorization: Bearer {TOKEN}"
   ```

3. **Revise a documentação:**
   - `/docs/FRONTEND_UPDATE_PHASE2.md` - Esta documentação
   - `/docs/IMPLEMENTACAO_NOVAS_FUNCIONALIDADES_FASE2.md` - Backend

---

## ✨ PARABÉNS! 🎉

Se todos os testes passaram, seu frontend está totalmente integrado com as novas funcionalidades do backend!

**Próximos passos:**
- [ ] Deploy para staging/produção
- [ ] Testes com dados reais
- [ ] Feedback dos usuários
- [ ] Melhorias com base no feedback

---

**Status:** ✅ PRONTO PARA TESTAR

Todos os componentes estão implementados e conectados ao backend.

