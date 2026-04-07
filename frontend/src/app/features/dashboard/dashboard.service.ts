import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DashboardStatsResponse {
  botCount: number;
  activeConversationCount: number;
  totalMessageCount: number;
  userCount: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private apiBase = environment.apiUrl || '';
  private api = `${this.apiBase}/api/analytics/dashboard/stats`;

  constructor(private http: HttpClient) {}

  getStats(): Observable<DashboardStatsResponse> {
    return this.http.get<DashboardStatsResponse>(this.api);
  }
}

