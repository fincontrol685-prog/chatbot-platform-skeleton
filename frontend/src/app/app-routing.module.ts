import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth.guard';

const routes: Routes = [
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard],
    data: {
      title: 'Dashboard executivo',
      description: 'Acompanhe indicadores de operacao, volume e capacidade em tempo real.'
    }
  },
  {
    path: 'login',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'bots',
    loadChildren: () => import('./features/bots/bots.module').then(m => m.BotsModule),
    canActivate: [AuthGuard],
    data: {
      title: 'Gestao de bots',
      description: 'Organize o portfolio de bots, configuracoes e disponibilidade do ambiente.'
    }
  },
  {
    path: 'conversations',
    loadChildren: () => import('./features/conversations/conversations.module').then(m => m.ConversationsModule),
    canActivate: [AuthGuard],
    data: {
      title: 'Central de conversas',
      description: 'Supervisione interacoes, priorize fluxos e acompanhe o historico operacional.'
    }
  },
  {
    path: 'professional',
    loadChildren: () => import('./features/professional-management/professional-management.module').then(m => m.ProfessionalManagementModule),
    canActivate: [AuthGuard],
    data: {
      title: 'Estrutura organizacional',
      description: 'Mapeie departamentos, equipes e responsabilidades da operacao.'
    }
  },
  {
    path: 'compliance',
    loadChildren: () => import('./features/compliance-security/compliance-security.module').then(m => m.ComplianceSecurityModule),
    canActivate: [AuthGuard],
    data: {
      title: 'Seguranca e compliance',
      description: 'Gerencie consentimento, autenticacao reforcada e controles de risco.'
    }
  },
  {
    path: 'analytics-advanced',
    loadChildren: () => import('./features/advanced-analytics/advanced-analytics.module').then(m => m.AdvancedAnalyticsModule),
    canActivate: [AuthGuard],
    data: {
      title: 'Analytics avancado',
      description: 'Explore relatorios, metricas e sinais estrategicos da plataforma.'
    }
  },
  {
    path: 'audit-logs',
    loadComponent: () => import('./features/audit-logs/audit-logs.component').then(m => m.AuditLogsComponent),
    canActivate: [AuthGuard],
    data: {
      title: 'Auditoria operacional',
      description: 'Investigue eventos, alteracoes criticas e rastreabilidade do sistema.'
    }
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
