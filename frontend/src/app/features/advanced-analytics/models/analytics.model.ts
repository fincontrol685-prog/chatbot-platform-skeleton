export interface AnalyticsMetricDto {
  id?: number;
  botId?: number;
  teamId?: number;
  departmentId?: number;
  userId?: number;
  metricType: string; // CONVERSATION_COUNT, RESPONSE_TIME, SATISFACTION, etc.
  metricValue: number;
  dimension?: string; // daily, hourly, etc.
  periodDate: string; // YYYY-MM-DD
  createdAt?: string;
}

export interface CustomReportDto {
  id?: number;
  name: string;
  description?: string;
  createdBy?: number;
  isPublic: boolean;
  reportDefinition: ReportDefinition;
  metricTypes: string[];
  filters?: ReportFilters;
  createdAt?: string;
  updatedAt?: string;
}

export interface ReportDefinition {
  title: string;
  type: 'TABLE' | 'CHART' | 'DASHBOARD';
  chartType?: 'LINE' | 'BAR' | 'PIE' | 'AREA';
  groupBy?: string; // 'bot', 'team', 'department', 'date'
  timeRange?: {
    startDate: string;
    endDate: string;
  };
}

export interface ReportFilters {
  botIds?: number[];
  teamIds?: number[];
  departmentIds?: number[];
  userIds?: number[];
  dateRange?: {
    from: string;
    to: string;
  };
}

export interface ExportRequest {
  format: 'EXCEL' | 'CSV' | 'PDF';
  reportId?: number;
  metrics?: AnalyticsMetricDto[];
  filters?: ReportFilters;
}

export interface ExportResponse {
  downloadUrl: string;
  fileName: string;
  fileSize?: number;
}

export interface MetricsAggregateDto {
  period: string;
  metrics: { [key: string]: number };
  timestamp: string;
}

