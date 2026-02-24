import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BotAnalytics {
  totalConversations: number;
  activeConversations: number;
  totalMessages: number;
  averageResponseTime: number;
  averageSentimentScore: number;
  messageVolumeByDay: any[];
}

export interface DashboardStats {
  botCount: number;
  activeConversationCount: number;
  totalMessageCount: number;
  userCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private apiUrl = '/api/analytics';

  constructor(private http: HttpClient) {}

  getBotAnalytics(botId: number): Observable<BotAnalytics> {
    return this.http.get<BotAnalytics>(`${this.apiUrl}/bots/${botId}`);
  }

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`);
  }

  getMessageVolume(botId: number, days: number = 30): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/bots/${botId}/message-volume?days=${days}`);
  }

  getSentimentAnalysis(botId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/bots/${botId}/sentiment`);
  }

  getIntentDistribution(botId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/bots/${botId}/intent-distribution`);
  }
}

