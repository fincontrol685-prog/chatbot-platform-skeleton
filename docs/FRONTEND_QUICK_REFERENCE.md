# 🎯 REFERÊNCIA RÁPIDA - FRONTEND ATUALIZADO

## 📍 Acessar os Novos Módulos

| Funcionalidade | URL | Componente Principal |
|---|---|---|
| 🏢 **Departamentos** | `http://localhost:4200/professional/departments` | `DepartmentListComponent` |
| 👥 **Equipes** | `http://localhost:4200/professional/teams` | `TeamListComponent` |
| 🔐 **2FA Setup** | `http://localhost:4200/compliance/security` | `TwoFactorSetupComponent` |
| ✅ **Consentimento GDPR** | `http://localhost:4200/compliance/consent` | `ConsentManagerComponent` |
| 📊 **Dashboard Métricas** | `http://localhost:4200/analytics-advanced/dashboard` | `MetricsDashboardComponent` |
| 📋 **Relatórios** | `http://localhost:4200/analytics-advanced/reports` | `ReportsListComponent` |

---

## 📁 Localização dos Arquivos

### Professional Management
```
frontend/src/app/features/professional-management/
├── models/department.model.ts
├── professional-management.service.ts
├── department-list/
├── department-form/
├── team-list/
├── team-form/
└── professional-management.module.ts
```

### Compliance Security
```
frontend/src/app/features/compliance-security/
├── models/compliance.model.ts
├── compliance-security.service.ts
├── two-factor-setup/
├── consent-manager/
└── compliance-security.module.ts
```

### Advanced Analytics
```
frontend/src/app/features/advanced-analytics/
├── models/analytics.model.ts
├── advanced-analytics.service.ts
├── metrics-dashboard/
├── reports-list/
├── report-form/
└── advanced-analytics.module.ts
```

---

## 🔧 Comandos Úteis

### Iniciar Frontend
```bash
cd frontend
npm install
npm start
# Acesse em http://localhost:4200
```

### Build para Produção
```bash
cd frontend
npm run build
# Arquivos em: dist/
```

### Executar Testes
```bash
cd frontend
npm test
```

### Lint
```bash
cd frontend
npm run lint
```

---

## 📡 Endpoints por Módulo

### Professional Management (24 endpoints)
```
[POST]   /api/departments
[GET]    /api/departments
[GET]    /api/departments/{id}
[GET]    /api/departments/root
[GET]    /api/departments/{id}/hierarchy
[GET]    /api/departments/{id}/subdepartments
[GET]    /api/departments/search?q=...
[PUT]    /api/departments/{id}
[DELETE] /api/departments/{id}

[POST]   /api/teams
[GET]    /api/teams
[GET]    /api/teams/{id}
[GET]    /api/teams/department/{departmentId}
[GET]    /api/teams/search?q=...
[PUT]    /api/teams/{id}
[DELETE] /api/teams/{id}
[POST]   /api/teams/{id}/members/{userId}
[DELETE] /api/teams/{id}/members/{userId}
[PUT]    /api/teams/{id}/lead/{userId}
[GET]    /api/teams/{id}/members
```

### Compliance Security (18 endpoints)
```
[POST]   /api/security/2fa/init
[POST]   /api/security/2fa/verify
[POST]   /api/security/2fa/validate
[GET]    /api/security/2fa/status
[DELETE] /api/security/2fa

[POST]   /api/compliance/consent
[DELETE] /api/compliance/consent/{type}
[GET]    /api/compliance/consent/my

[POST]   /api/compliance/data-deletion/request
[GET]    /api/compliance/data-deletion/my
[GET]    /api/compliance/data-deletion/pending
[PUT]    /api/compliance/data-deletion/{id}/approve
[PUT]    /api/compliance/data-deletion/{id}/reject
```

### Advanced Analytics (16+ endpoints)
```
[POST]   /api/analytics-advanced/metrics
[GET]    /api/analytics-advanced/bot/{id}/metrics
[GET]    /api/analytics-advanced/team/{id}/metrics
[GET]    /api/analytics-advanced/department/{id}/metrics

[POST]   /api/analytics-advanced/reports
[GET]    /api/analytics-advanced/reports
[GET]    /api/analytics-advanced/reports/my
[GET]    /api/analytics-advanced/reports/{id}
[PUT]    /api/analytics-advanced/reports/{id}
[DELETE] /api/analytics-advanced/reports/{id}

[POST]   /api/analytics-advanced/export/excel
[POST]   /api/analytics-advanced/export/csv
[GET]    (download endpoints)
```

---

## 💻 Services TypeScript

### Usar em Componentes
```typescript
// Professional Management
import { ProfessionalManagementService } from '../professional-management.service';

constructor(private service: ProfessionalManagementService) {}

this.service.listDepartments().subscribe(data => { ... });
this.service.createTeam(teamData).subscribe(result => { ... });

// Compliance
import { ComplianceSecurityService } from '../compliance-security.service';

constructor(private service: ComplianceSecurityService) {}

this.service.initiateTwoFactorAuth().subscribe(result => { ... });
this.service.grantConsent('MARKETING').subscribe(data => { ... });

// Analytics
import { AdvancedAnalyticsService } from '../advanced-analytics.service';

constructor(private service: AdvancedAnalyticsService) {}

this.service.getBotMetrics(botId).subscribe(metrics => { ... });
this.service.createReport(reportData).subscribe(result => { ... });
```

---

## 🎨 Material Components Usados

| Componente | Onde Usado | Exemplo |
|---|---|---|
| `MatCard` | Todos os módulos | Containers de conteúdo |
| `MatForm` | Formulários | Department, Team, Reports |
| `MatList` | Listas | Departamentos, Equipes |
| `MatTable` | Tabelas | Dashboard de métricas |
| `MatButton` | Ações | Criar, Editar, Deletar |
| `MatIcon` | UI | Ícones em botões |
| `MatSpinner` | Loading | Durante requisições HTTP |
| `MatAlert` | Mensagens | Erros e sucessos |
| `MatSelect` | Dropdowns | Filtros |
| `MatCheckbox` | Seleção | Consentimentos |

---

## 🔐 Autenticação

### Como Funciona
1. User faz login em `/login`
2. Backend retorna JWT token
3. Token armazenado em localStorage
4. Interceptor adiciona token automaticamente em todas requisições HTTP:
   ```
   Authorization: Bearer {TOKEN}
   ```
5. AuthGuard protege rotas privadas

### Rotas Protegidas
```typescript
{
  path: 'professional',
  loadChildren: () => import(...),
  canActivate: [AuthGuard]  // ← Requer login
}
```

---

## 📊 Fluxos Principais

### 1. Criar Departamento
```
1. User navega para /professional/departments
2. Clica em "Novo Departamento"
3. Preenche formulário
4. Clica em "Criar"
5. FormComponent faz POST /api/departments
6. Backend retorna novo departamento
7. ListComponent recarrega lista
8. Novo item aparece na tabela
```

### 2. Ativar 2FA
```
1. User navega para /compliance/security
2. Clica em "Ativar 2FA"
3. Sistema faz POST /api/security/2fa/init
4. QR Code aparece
5. User escaneia com Google Authenticator
6. User insere código 6 dígitos
7. Sistema faz POST /api/security/2fa/verify
8. Backend ativa 2FA
9. Backup codes são exibidos
```

### 3. Exportar Métricas
```
1. User navega para /analytics-advanced/dashboard
2. Seleciona filtro e ID
3. Clica em "Buscar"
4. Métricas carregam em tabela
5. Clica em "Exportar Excel"
6. Dashboard faz POST /api/analytics-advanced/export/excel
7. Backend retorna download URL
8. Browser baixa arquivo automaticamente
```

---

## 🆘 Troubleshooting Rápido

| Problema | Causa | Solução |
|----------|-------|---------|
| 404 no endpoint | Backend não rodando | Inicie backend: `./mvnw spring-boot:run` |
| CORS error | Configuração CORS | Adicione frontend URL no CORS config |
| Autenticação falha | Token inválido | Faça logout/login |
| QR Code não aparece | Relógio dessincronizado | Sincronize data/hora do sistema |
| Arquivo não baixa | Download URL inválida | Verifique se endpoint export existe |
| Tabela vazia | Sem dados | Insira dados no banco antes |

---

## 📚 Documentação

| Documento | Localização | Conteúdo |
|-----------|------------|----------|
| Implementação Completa | `docs/FRONTEND_UPDATE_PHASE2.md` | Todas as features implementadas |
| Guia de Testes | `docs/FRONTEND_TESTING_GUIDE.md` | Testes passo-a-passo |
| Checklist | `docs/FRONTEND_CHECKLIST.md` | Resumo executivo |
| Backend Docs | `docs/IMPLEMENTACAO_NOVAS_FUNCIONALIDADES_FASE2.md` | Implementação backend |

---

## 🎯 Próximos Passos

1. **Testes**
   ```bash
   cd frontend
   npm test
   ```

2. **Build**
   ```bash
   npm run build
   ```

3. **Deploy**
   - Copiar pasta `dist/` para servidor web
   - Servir com Nginx/Apache
   - Configurar proxy para API backend

4. **Monitoramento**
   - Verificar logs no navegador (F12)
   - Monitorar requests HTTP
   - Alertar em caso de erros

---

## 🚀 Deploy Checklist

- [ ] Backend rodando em produção
- [ ] Frontend build otimizado (`npm run build`)
- [ ] CORS configurado corretamente
- [ ] JWT token refresh implementado
- [ ] Erro handling em produção
- [ ] HTTPS habilitado
- [ ] CDN configurado (opcional)
- [ ] Monitoramento ativo

---

## 📞 Contato & Suporte

Para problemas ou dúvidas:
1. Verifique a documentação em `/docs/`
2. Revise os logs (F12 no navegador)
3. Teste endpoints com curl
4. Verifique backend logs

---

**Última atualização:** 2026-04-07  
**Status:** ✅ PRONTO PARA PRODUÇÃO

