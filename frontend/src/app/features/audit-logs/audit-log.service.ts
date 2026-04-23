import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuditLog } from './audit-logs.component';

interface AuditLogPage {
  content: AuditLog[];
  totalElements: number;
}

@Injectable({ providedIn: 'root' })
export class AuditLogService {
  private apiBase = environment.apiUrl || '';
  private apiUrl = `${this.apiBase}/api/audit-logs`;

  constructor(private readonly http: HttpClient) {}

  list(filters: { action?: string; entityType?: string; status?: string; page: number; size: number }): Observable<AuditLogPage> {
    let params = new HttpParams()
      .set('page', filters.page.toString())
      .set('size', filters.size.toString());

    if (filters.action) {
      params = params.set('action', filters.action);
    }

    if (filters.entityType) {
      params = params.set('entityType', filters.entityType);
    }

    if (filters.status) {
      params = params.set('status', filters.status);
    }

    return this.http.get<AuditLogPage>(this.apiUrl, { params });
  }
}
