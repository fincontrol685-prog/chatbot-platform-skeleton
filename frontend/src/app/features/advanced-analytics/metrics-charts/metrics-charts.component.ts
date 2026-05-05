import { Component, Input, OnInit } from '@angular/core';
import { AnalyticsMetricDto } from '../models/analytics.model';

interface ChartData {
  name: string;
  value: number;
  series?: any[];
}

@Component({
    selector: 'app-metrics-charts',
    templateUrl: './metrics-charts.component.html',
    styles: [`
    .charts-container {
      padding: 20px;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .chart-card {
      margin-bottom: 20px;
    }

    .pie-chart-center {
      display: flex;
      justify-content: center;
      align-items: center;
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

    ngx-charts-line-chart,
    ngx-charts-bar-horizontal,
    ngx-charts-pie-chart,
    ngx-charts-area-chart {
      display: flex;
      justify-content: center;
    }

    mat-card-header {
      margin-bottom: 15px;
    }

    mat-card-title {
      font-size: 16px;
      color: #1976d2;
    }

    @media (max-width: 768px) {
      .chart-card {
        margin-bottom: 15px;
      }

      ngx-charts-line-chart,
      ngx-charts-area-chart {
        max-width: 100%;
        overflow-x: auto;
      }
    }
  `],
    standalone: false
})
export class MetricsChartsComponent implements OnInit {
  @Input() metrics: AnalyticsMetricDto[] = [];
  @Input() chartType: 'line' | 'bar' | 'pie' | 'area' = 'line';

  lineChartData: any[] = [];
  barChartData: any[] = [];
  pieChartData: any[] = [];
  areaChartData: any[] = [];

  // Chart configuration
  colorScheme = {
    domain: ['#1976d2', '#388e3c', '#f57c00', '#c2185b', '#7b1fa2', '#0097a7']
  };

  xAxisLabel = 'Data';
  yAxisLabel = 'Valor';

  ngOnInit(): void {
    this.processMetrics();
  }

  ngOnChanges(): void {
    this.processMetrics();
  }

  processMetrics(): void {
    if (!this.metrics || this.metrics.length === 0) {
      return;
    }

    // Agrupar metrics por tipo e data
    const groupedByType: { [key: string]: AnalyticsMetricDto[] } = {};
    const groupedByDate: { [key: string]: AnalyticsMetricDto[] } = {};

    this.metrics.forEach(metric => {
      if (!groupedByType[metric.metricType]) {
        groupedByType[metric.metricType] = [];
      }
      groupedByType[metric.metricType].push(metric);

      if (!groupedByDate[metric.periodDate]) {
        groupedByDate[metric.periodDate] = [];
      }
      groupedByDate[metric.periodDate].push(metric);
    });

    // Processar dados para diferentes tipos de gráficos
    this.processLineChartData(groupedByDate);
    this.processBarChartData(groupedByType);
    this.processPieChartData(groupedByType);
    this.processAreaChartData(groupedByDate);
  }

  private processLineChartData(groupedByDate: { [key: string]: AnalyticsMetricDto[] }): void {
    const metricsMap: { [key: string]: { name: string; series: any[] } } = {};

    Object.entries(groupedByDate).forEach(([date, metrics]) => {
      metrics.forEach(metric => {
        if (!metricsMap[metric.metricType]) {
          metricsMap[metric.metricType] = { name: metric.metricType, series: [] };
        }
        metricsMap[metric.metricType].series.push({
          name: date,
          value: metric.metricValue
        });
      });
    });

    this.lineChartData = Object.values(metricsMap);
  }

  private processBarChartData(groupedByType: { [key: string]: AnalyticsMetricDto[] }): void {
    const data: ChartData[] = [];

    Object.entries(groupedByType).forEach(([type, metrics]) => {
      const sum = metrics.reduce((acc, m) => acc + m.metricValue, 0);
      const avg = sum / metrics.length;
      data.push({
        name: type,
        value: avg
      });
    });

    this.barChartData = data;
  }

  private processPieChartData(groupedByType: { [key: string]: AnalyticsMetricDto[] }): void {
    const data: ChartData[] = [];

    Object.entries(groupedByType).forEach(([type, metrics]) => {
      const sum = metrics.reduce((acc, m) => acc + m.metricValue, 0);
      data.push({
        name: type,
        value: sum
      });
    });

    this.pieChartData = data;
  }

  private processAreaChartData(groupedByDate: { [key: string]: AnalyticsMetricDto[] }): void {
    const metricsMap: { [key: string]: { name: string; series: any[] } } = {};

    Object.entries(groupedByDate).forEach(([date, metrics]) => {
      metrics.forEach(metric => {
        if (!metricsMap[metric.metricType]) {
          metricsMap[metric.metricType] = { name: metric.metricType, series: [] };
        }
        metricsMap[metric.metricType].series.push({
          name: date,
          value: metric.metricValue
        });
      });
    });

    this.areaChartData = Object.values(metricsMap);
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

