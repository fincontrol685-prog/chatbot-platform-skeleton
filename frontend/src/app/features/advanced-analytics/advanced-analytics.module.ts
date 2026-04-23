import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ReportsListComponent } from './reports-list/reports-list.component';
import { ReportFormComponent } from './report-form/report-form.component';
import { MetricsDashboardComponent } from './metrics-dashboard/metrics-dashboard.component';
import { MetricsChartsComponent } from './metrics-charts/metrics-charts.component';
import { AdvancedAnalyticsService } from './advanced-analytics.service';

@NgModule({
  declarations: [
    ReportsListComponent,
    ReportFormComponent,
    MetricsDashboardComponent,
    MetricsChartsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    NgxChartsModule,
    RouterModule.forChild([
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        component: MetricsDashboardComponent,
        data: {
          title: 'Central de monitoramento',
          description: 'Veja desempenho por bot, conversas ativas e sinais operacionais.'
        }
      },
      {
        path: 'reports',
        component: ReportsListComponent,
        data: {
          title: 'Relatorios analiticos',
          description: 'Consulte e organize relatorios compartilhados e pessoais.'
        }
      }
    ])
  ],
  providers: [AdvancedAnalyticsService]
})
export class AdvancedAnalyticsModule { }
