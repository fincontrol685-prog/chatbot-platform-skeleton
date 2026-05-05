# 🔧 Correções - Envio de Mensagens em Bot Novo

## Problema
Ao tentar enviar uma mensagem em um bot recém-criado, a mensagem não estava sendo processada corretamente.

## Raiz do Problema

1. **HTML Entity Encoding em Configuração do Bot**
   - Quando a configuração do bot era enviada via formulário HTML/JSON, as aspas duplas (`"`) eram codificadas como `&quot;`
   - O símbolo `&` era codificado como `&amp;`
   - Isso causava erro ao fazer parsing JSON: `Unexpected character ('&')`

2. **Bot Desativado**
   - Bots poderiam ser criados mas não estarem habilitados para responder
   - Não havia validação adequada antes de processar mensagens

3. **Usuário Sistema Desativado**
   - O usuário `bot.system` (necessário para o bot enviar respostas) estava desativado por padrão
   - Isso impedia que as respostas fossem atribuídas ao bot

4. **Falta de Tratamento de Erros**
   - Não havia logs adequados para diagnosticar problemas no envio de mensagens

## 🔧 Soluções Implementadas

### 1. **BotService.java** - Decodificação de HTML Entities
```java
// Adicionada decodificação de HTML entities antes de fazer parsing JSON
String decodedConfig = StringEscapeUtils.unescapeHtml4(trimmedConfig);
JsonNode jsonNode = objectMapper.readTree(decodedConfig);
```

**Mudanças:**
- Importado `StringEscapeUtils` do `org.apache.commons.text`
- Método `normalizeConfig()` agora decodifica HTML entities antes de validar JSON
- Converte `&quot;` → `"`, `&amp;` → `&`, etc.

### 2. **ConversationMessageService.java** - Validações e Tratamento de Erros
```java
// Validação: Bot deve estar habilitado
if (!Boolean.TRUE.equals(conversation.getBot().getEnabled())) {
    throw new IllegalArgumentException("Bot desativado. Nao e possivel enviar mensagens");
}

// Tratamento de erros com logging
try {
    // ... processar mensagem ...
} catch (Exception ex) {
    log.error("Erro ao processar mensagem para conversa {}: {}", conversationId, ex.getMessage(), ex);
    throw new IllegalArgumentException("Erro ao processar sua mensagem: " + ex.getMessage(), ex);
}
```

**Mudanças:**
- ✅ Adicionado logger para diagnóstico
- ✅ Validação se bot está habilitado
- ✅ Validação se bot existe
- ✅ Mensagens de erro mais informativas
- ✅ Try-catch com logging para capturar erros específicos

### 3. **DataInitializer.java** - Sistema User Habilitado
```java
// Sistema user agora inicia habilitado (era desativado antes)
user.setEnabled(true);
```

**Mudanças:**
- ✅ Usuário `bot.system` agora é criado com `enabled = true`
- ✅ Permitir que bots enviem respostas imediatamente após criação

### 4. **BotResponseService.java** - Decodificação Defensiva
```java
// Decodificação HTML entities como camada defensiva
String decodedConfig = StringEscapeUtils.unescapeHtml4(rawConfig);
BotConfig parsedConfig = objectMapper.readValue(decodedConfig, BotConfig.class);
```

**Mudanças:**
- ✅ Adicionada decodificação HTML entities no método `readConfig()`
- ✅ Proteção extra se configuração vir com HTML entities codificadas

## ✅ Validações Implementadas

```
Bot Criado
    ↓
┌─ Conversa Ativa? ─────→ ❌ Erro: "A conversa nao esta ativa"
│
✓ Sim
    ↓
┌─ Bot Existe? ─────→ ❌ Erro: "Bot nao encontrado para esta conversa"
│
✓ Sim
    ↓
┌─ Bot Habilitado? ─────→ ❌ Erro: "Bot desativado. Nao e possivel enviar mensagens"
│
✓ Sim
    ↓
┌─ Usuário Sistema Existe? ─────→ ❌ Erro: "Usuario interno do bot nao encontrado..."
│
✓ Sim
    ↓
✅ MENSAGEM ENVIADA COM SUCESSO
```

## 📋 Checklist de Verificação

- [x] HTML entities decodificadas antes de parsing JSON
- [x] Bot status validado antes de processar mensagem
- [x] Usuário sistema (`bot.system`) habilitado por padrão
- [x] Logger adicionado para diagnóstico
- [x] Mensagens de erro melhoradas
- [x] Tratamento de exceções robusto
- [x] Código compila sem erros

## 🧪 Como Testar

1. **Criar um novo bot:**
   ```
   POST /api/bots
   {
     "name": "Teste Bot",
     "key": "teste-bot",
     "enabled": true,
     "config": { "profile": { ... }, "messages": { ... } }
   }
   ```

2. **Criar uma conversa:**
   ```
   POST /api/conversations
   {
     "botId": 1,
     "title": "Chat Teste"
   }
   ```

3. **Enviar uma mensagem:**
   ```
   POST /api/messages/conversation/1/exchange
   {
     "content": "Olá!"
   }
   ```

4. **Verificar resposta:**
   - A resposta agora deve chegar com sucesso
   - Aparecerá tanto a mensagem do usuário quanto a resposta do bot

## 🔍 Logs para Diagnóstico

Se houver problemas, verifique os logs da aplicação:

```
# Sucesso
Bot updated successfully with id: 1
Erro ao processar... (se houver exceção, será logado aqui)

# Erros possíveis
- "Bot desativado. Nao e possivel enviar mensagens"
- "A conversa nao esta ativa"
- "Bot nao encontrado para esta conversa"
- "Usuario interno do bot nao encontrado ou desativado"
```

## 📝 Dependências Já Existentes

- ✅ `org.apache.commons:commons-text` (v1.10.0) - já estava no `pom.xml`
- ✅ Jackson para JSON processing
- ✅ Spring Boot Transaction Management

## 🚀 Próximos Passos Recomendados

1. Adicionar endpoint de diagnóstico para validar saúde do bot
2. Implementar webhook para notificar quando bot fica indisponível
3. Adicionar métricas de falha no envio de mensagens
4. Considerar adicionar validação da configuração no momento da criação do bot

---

**Versão:** 1.0  
**Data:** 2026-05-04  
**Status:** ✅ Implementado e Testado

