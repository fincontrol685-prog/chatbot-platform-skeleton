import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AdvancedAnalyticsService } from '../advanced-analytics.service';
import { AnalyticsMetricDto } from '../models/analytics.model';

@Component({
  selector: 'app-metrics-dashboard',
  templateUrl: './metrics-dashboard.component.html',
  styles: [`
    .container {
      padding: 20px;
    }

    h2 {
      margin-bottom: 20px;
      color: #1976d2;
    }

    .filter-card {
      margin-bottom: 20px;
    }

    .filter-row {
      display: flex;
      gap: 15px;
      margin-bottom: 15px;
      flex-wrap: wrap;
      align-items: flex-end;
    }

    mat-form-field {
      min-width: 150px;
    }

    .filter-actions {
      display: flex;
      gap: 10px;
      margin-top: 15px;
    }

    .loading {
      display: flex;
      justify-content: center;
      padding: 40px 20px;
    }

    .metrics-card {
      margin-bottom: 20px;
    }

    .charts-card {
      margin-bottom: 20px;
    }

    .table-container {
      overflow-x: auto;
      max-height: 600px;
      overflow-y: auto;
    }

    .metrics-table {
      width: 100%;
      border-collapse: collapse;
    }

    table th {
      background-color: #f5f5f5;
      font-weight: 600;
      color: #333;
      position: sticky;
      top: 0;
    }

    table td {
      padding: 12px;
      border-bottom: 1px solid #eee;
    }

    table tr:hover {
      background-color: #f9f9f9;
    }

    .export-actions {
      display: flex;
      gap: 10px;
      margin-top: 20px;
      padding-top: 15px;
      border-top: 1px solid #ddd;
    }

    .empty-state {
      text-align: center;
      padding: 60px 20px;
      color: #666;
    }

    .empty-state mat-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      margin-bottom: 15px;
      opacity: 0.2;
    }

    p {
      margin: 10px 0;
      line-height: 1.6;
    }
  `]
})
export class MetricsDashboardComponent implements OnInit {
  filterForm!: FormGroup;
  metrics: AnalyticsMetricDto[] = [];
  loading = false;
  error: string | null = null;

  displayColumns = ['metricType', 'metricValue', 'botId', 'teamId', 'periodDate'];

  constructor(
    private fb: FormBuilder,
    private service: AdvancedAnalyticsService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.filterForm = this.fb.group({
      filterType: ['bot'], // bot, team, department
      filterId: [null],
      startDate: [''],
      endDate: ['']
    });
  }

  loadMetrics(): void {
    const filterType = this.filterForm.get('filterType')?.value;
    const filterId = this.filterForm.get('filterId')?.value;

    if (!filterId) {
      this.error = 'Selecione uma opção para filtrar';
      return;
    }

    this.loading = true;
    this.error = null;

    const filters = {
      dateRange: {
        from: this.filterForm.get('startDate')?.value || '',
        to: this.filterForm.get('endDate')?.value || ''
      }
    };

    let request;
    switch (filterType) {
      case 'bot':
        request = this.service.getBotMetrics(filterId, filters);
        break;
      case 'team':
        request = this.service.getTeamMetrics(filterId, filters);
        break;
      case 'department':
        request = this.service.getDepartmentMetrics(filterId, filters);
        break;
      default:
        this.error = 'Tipo de filtro inválido';
        this.loading = false;
        return;
    }

    request.subscribe({
      next: (data) => {
        this.metrics = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar métricas: ' + err.message;
        this.loading = false;
      }
    });
  }

  exportToExcel(): void {
    if (this.metrics.length === 0) {
      this.error = 'Nenhuma métrica para exportar';
      return;
    }

    this.loading = true;
    this.service.exportToExcel({ metrics: this.metrics }).subscribe({
      next: (response) => {
        this.downloadFile(response.downloadUrl, response.fileName);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao exportar: ' + err.message;
        this.loading = false;
      }
    });
  }

  exportToCsv(): void {
    if (this.metrics.length === 0) {
      this.error = 'Nenhuma métrica para exportar';
      return;
    }

    this.loading = true;
    this.service.exportToCsv({ metrics: this.metrics }).subscribe({
      next: (response) => {
        this.downloadFile(response.downloadUrl, response.fileName);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao exportar: ' + err.message;
        this.loading = false;
      }
    });
  }

  private downloadFile(url: string, fileName: string): void {
    this.service.downloadExport(url).subscribe({
      next: (blob) => {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
        link.click();
        window.URL.revokeObjectURL(link.href);
      },
      error: (err) => {
        this.error = 'Erro ao baixar arquivo: ' + err.message;
      }
    });
  }

  getMetricLabel(type: string): string {
    const labels: { [key: string]: string } = {
      'CONVERSATION_COUNT': 'Contagem de Conversas',
      'RESPONSE_TIME': 'Tempo de Resposta',
      'SATISFACTION_SCORE': 'Score de Satisfação',
      'RESOLUTION_TIME': 'Tempo de Resolução',
      'BOT_INTERACTIONS': 'Interações de Bot',
      'USER_RETENTION': 'Retenção de Usuários'
    };
    return labels[type] || type;
  }
}

