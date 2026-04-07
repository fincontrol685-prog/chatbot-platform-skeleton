# 🚀 GUIA RÁPIDO DE INTEGRAÇÃO - Fase 1

## ⚡ Em 5 Minutos

### Passo 1: Atualizar `pom.xml` (opcional - já está incluído)
```xml
<!-- Já incluído no seu projeto -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Passo 2: Rodar aplicação
```bash
cd /home/robertojr/chatbot-platform-skeleton
mvn clean install
mvn spring-boot:run
```

**O que acontece:**
- ✅ Flyway roda V1, V2, V3 automaticamente
- ✅ Tabelas CONVERSATION, CONVERSATION_MESSAGE, AUDIT_LOG criadas
- ✅ Índices de performance criados
- ✅ Aplicação inicia em http://localhost:8080

### Passo 3: Testar endpoints

```bash
# 1. Fazer login (obter JWT token)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Salvar o token em uma variável
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# 2. Listar bots
curl -X GET http://localhost:8080/api/bots \
  -H "Authorization: Bearer $TOKEN"

# 3. Criar conversa
curl -X POST http://localhost:8080/api/conversations \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "botId": 1,
    "userId": 1,
    "title": "Conversa de Teste"
  }'

# 4. Adicionar mensagem
curl -X POST http://localhost:8080/api/messages/conversation/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "messageType": "USER",
    "content": "Olá, como funciona seu chatbot?"
  }'

# 5. Ver histórico
curl -X GET http://localhost:8080/api/messages/conversation/1/history \
  -H "Authorization: Bearer $TOKEN"

# 6. Ver analytics
curl -X GET http://localhost:8080/api/analytics/dashboard/stats \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🎨 Integração no Frontend

### Passo 1: Atualizar `app-routing.module.ts`

```typescript
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  {
    path: 'conversations',
    loadChildren: () => import('./features/conversations/conversations.module')
      .then(m => m.ConversationsModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'bots',
    loadChildren: () => import('./features/bots/bots.module')
      .then(m => m.BotsModule),
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

### Passo 2: Atualizar navegação no `app.component.html`

```html
<mat-toolbar color="primary">
  <span>Chatbot Platform</span>
  <span class="spacer"></span>
  <button mat-icon-button [matMenuTriggerFor]="menu">
    <mat-icon>menu</mat-icon>
  </button>
  <mat-menu #menu="matMenu">
    <button mat-menu-item routerLink="/dashboard">
      <mat-icon>dashboard</mat-icon>
      <span>Dashboard</span>
    </button>
    <button mat-menu-item routerLink="/bots">
      <mat-icon>smart_toy</mat-icon>
      <span>Bots</span>
    </button>
    <button mat-menu-item routerLink="/conversations">
      <mat-icon>chat_bubble</mat-icon>
      <span>Conversas</span>
    </button>
    <mat-divider></mat-divider>
    <button mat-menu-item (click)="logout()">
      <mat-icon>logout</mat-icon>
      <span>Logout</span>
    </button>
  </mat-menu>
</mat-toolbar>

<router-outlet></router-outlet>
```

### Passo 3: Importar módulos no `app.module.ts`

```typescript
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
    // ... outros módulos
  ],
  providers: [AuthGuard, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

### Passo 4: Adicionar rotas específicas de conversas (opcional)

```typescript
// Em bots-list.component.ts
onViewConversations(botId: number) {
  this.router.navigate(['/bots', botId, 'conversations']);
}

// Em app-routing.module.ts
{
  path: 'bots/:botId/conversations',
  component: ConversationListComponent
}
```

---

## 🔧 Correções Menores Necessárias

### 1. Adicionar métodos no `BotRepository.java`

```java
@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {
    // ... métodos existentes ...
    
    @Query("SELECT COUNT(b) FROM Bot b")
    long count();
}
```

### 2. Adicionar métodos no `UserRepository.java`

```java
@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    // ... métodos existentes ...
    
    @Query("SELECT COUNT(u) FROM UserAccount u")
    long count();
}
```

### 3. Corrigir `ConversationController.java` - método extractUserId

```java
private Long extractUserId(Authentication authentication) {
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Buscar ID do usuário pelo username
        return userRepository.findByUsername(userDetails.getUsername())
            .map(UserAccount::getId)
            .orElse(1L);
    }
    return 1L;
}
```

Adicione ao constructor:
```java
private final UserRepository userRepository;

public ConversationController(ConversationService conversationService, 
                             UserRepository userRepository) {
    this.conversationService = conversationService;
    this.userRepository = userRepository;
}
```

---

## 📱 Checklist de Testes

### Backend
- [ ] Migrations rodam sem erros (check logs)
- [ ] Endpoint POST /api/conversations retorna 201 Created
- [ ] Endpoint GET /api/conversations/{id} retorna a conversa
- [ ] Endpoint POST /api/messages/conversation/{id} adiciona mensagem
- [ ] Endpoint GET /api/messages/conversation/{id}/history retorna histórico
- [ ] Endpoint PATCH /api/conversations/{id}/close funciona
- [ ] Endpoint GET /api/audit-logs/user/{userId} mostra logs
- [ ] Endpoint GET /api/analytics/dashboard/stats retorna stats

### Frontend
- [ ] Dashboard carrega com ícones dos Material
- [ ] Tabela de conversas renderiza (mesmo que vazia)
- [ ] Interface de chat tem textarea e botão enviar
- [ ] Mensagens têm estilos USER vs BOT diferentes
- [ ] Botão "Fechar Conversa" funciona
- [ ] Scroll automático ao fim das mensagens

---

## 🐛 Troubleshooting

### Problema: Flyway não roda V2 e V3
**Solução:**
```bash
# Verificar logs
tail -f target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar

# Se migrations já foram rotas com V1, delete:
DELETE FROM flyway_schema_history WHERE version >= 2;

# Ou use:
mvn flyway:clean
```

### Problema: 401 Unauthorized nos testes
**Solução:** Envie o header correto:
```bash
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Problema: CORS error no frontend
**Solução:** Adicione ao `SecurityConfig.java`:
```java
http.cors().and()...
```

### Problema: Componentes Angular não carregam
**Solução:**
```bash
# Reinstalar dependencies
cd frontend
npm install
npm start
```

---

## 📊 Dados de Teste

### SQL para popular dados iniciais

```sql
-- Criar usuário de teste
INSERT INTO USER_ACCOUNT (username, email, password_hash, enabled) 
VALUES ('teste', 'teste@example.com', 'hashed_password', true);

-- Criar bot de teste
INSERT INTO BOT (name, bot_key, enabled, config) 
VALUES ('Bot Atendimento', 'bot_atendimento', true, '{}');

-- Criar conversa de teste
INSERT INTO CONVERSATION (bot_id, user_id, title, status, message_count, created_at, updated_at) 
VALUES (1, 1, 'Conversa de Teste', 'ACTIVE', 0, NOW(), NOW());

-- Adicionar mensagens de teste
INSERT INTO CONVERSATION_MESSAGE (conversation_id, sender_id, message_type, content, created_at) 
VALUES 
  (1, 1, 'USER', 'Olá!', NOW()),
  (1, 1, 'BOT', 'Oi! Como posso ajudar?', NOW()),
  (1, 1, 'USER', 'Qual é o horário?', NOW()),
  (1, 1, 'BOT', 'Estou disponível 24/7', NOW());
```

---

## 🎯 Próximos Passos

### Curto Prazo (Esta semana)
1. ✅ Integrar componentes no app.module.ts
2. ✅ Testar endpoints com Postman
3. ✅ Validar dados no banco
4. ⬜ Adicionar 2-3 testes unitários

### Médio Prazo (Este mês)
1. ⬜ Integrar WebSocket para chat em tempo real
2. ⬜ Adicionar gráficos no dashboard (Chart.js)
3. ⬜ Criar telas de relatórios
4. ⬜ Implementar webhooks

### Longo Prazo (Próximo trimestre)
1. ⬜ Integração com LLM (OpenAI)
2. ⬜ Sistema de templates
3. ⬜ Multi-tenancy
4. ⬜ Mobile app (React Native)

---

## 📞 Precisa de Ajuda?

- **Documentação:** Veja `IMPLEMENTACAO_FASE1.md`
- **Erros Compilação:** Verifique imports no IDE
- **Erro em Runtime:** Consulte console do backend e frontend
- **Questões SQL:** Use ferramenta como DBeaver

---

**Status:** Fase 1 Completa ✅
**Estimativa Integração:** 30-60 minutos
**Próxima Fase:** Dashboard com Gráficos + Webhooks

