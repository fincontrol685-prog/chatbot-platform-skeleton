import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth.guard';

const routes: Routes = [
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: 'login',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'bots',
    loadChildren: () => import('./features/bots/bots.module').then(m => m.BotsModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'conversations',
    loadChildren: () => import('./features/conversations/conversations.module').then(m => m.ConversationsModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'professional',
    loadChildren: () => import('./features/professional-management/professional-management.module').then(m => m.ProfessionalManagementModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'compliance',
    loadChildren: () => import('./features/compliance-security/compliance-security.module').then(m => m.ComplianceSecurityModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'analytics-advanced',
    loadChildren: () => import('./features/advanced-analytics/advanced-analytics.module').then(m => m.AdvancedAnalyticsModule),
    canActivate: [AuthGuard]
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

