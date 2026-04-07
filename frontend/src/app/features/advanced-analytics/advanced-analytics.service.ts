import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AnalyticsMetricDto,
  CustomReportDto,
  ExportRequest,
  ExportResponse,
  ReportFilters
} from './models/analytics.model';

@Injectable({ providedIn: 'root' })
export class AdvancedAnalyticsService {
  private apiBase = environment.apiUrl || '';
  private analyticsApi = `${this.apiBase}/api/analytics-advanced`;

  constructor(private http: HttpClient) {}

  // ===== METRICS =====
  recordMetric(metric: Partial<AnalyticsMetricDto>): Observable<AnalyticsMetricDto> {
    return this.http.post<AnalyticsMetricDto>(`${this.analyticsApi}/metrics`, metric);
  }

  getBotMetrics(botId: number, filters?: ReportFilters): Observable<AnalyticsMetricDto[]> {
    let params = new HttpParams();
    if (filters?.dateRange) {
      params = params.set('startDate', filters.dateRange.from);
      params = params.set('endDate', filters.dateRange.to);
    }
    return this.http.get<AnalyticsMetricDto[]>(
      `${this.analyticsApi}/bot/${botId}/metrics`,
      { params }
    );
  }

  getTeamMetrics(teamId: number, filters?: ReportFilters): Observable<AnalyticsMetricDto[]> {
    let params = new HttpParams();
    if (filters?.dateRange) {
      params = params.set('startDate', filters.dateRange.from);
      params = params.set('endDate', filters.dateRange.to);
    }
    return this.http.get<AnalyticsMetricDto[]>(
      `${this.analyticsApi}/team/${teamId}/metrics`,
      { params }
    );
  }

  getDepartmentMetrics(deptId: number, filters?: ReportFilters): Observable<AnalyticsMetricDto[]> {
    let params = new HttpParams();
    if (filters?.dateRange) {
      params = params.set('startDate', filters.dateRange.from);
      params = params.set('endDate', filters.dateRange.to);
    }
    return this.http.get<AnalyticsMetricDto[]>(
      `${this.analyticsApi}/department/${deptId}/metrics`,
      { params }
    );
  }

  // ===== CUSTOM REPORTS =====
  createReport(report: Partial<CustomReportDto>): Observable<CustomReportDto> {
    return this.http.post<CustomReportDto>(`${this.analyticsApi}/reports`, report);
  }

  listAccessibleReports(): Observable<CustomReportDto[]> {
    return this.http.get<CustomReportDto[]>(`${this.analyticsApi}/reports`);
  }

  getMyReports(): Observable<CustomReportDto[]> {
    return this.http.get<CustomReportDto[]>(`${this.analyticsApi}/reports/my`);
  }

  getReport(id: number): Observable<CustomReportDto> {
    return this.http.get<CustomReportDto>(`${this.analyticsApi}/reports/${id}`);
  }

  updateReport(id: number, report: Partial<CustomReportDto>): Observable<CustomReportDto> {
    return this.http.put<CustomReportDto>(`${this.analyticsApi}/reports/${id}`, report);
  }

  deleteReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.analyticsApi}/reports/${id}`);
  }

  // ===== EXPORT =====
  exportToExcel(request: Partial<ExportRequest>): Observable<ExportResponse> {
    return this.http.post<ExportResponse>(
      `${this.analyticsApi}/export/excel`,
      request
    );
  }

  exportToCsv(request: Partial<ExportRequest>): Observable<ExportResponse> {
    return this.http.post<ExportResponse>(
      `${this.analyticsApi}/export/csv`,
      request
    );
  }

  downloadExport(downloadUrl: string): Observable<Blob> {
    return this.http.get(downloadUrl, { responseType: 'blob' });
  }
}

