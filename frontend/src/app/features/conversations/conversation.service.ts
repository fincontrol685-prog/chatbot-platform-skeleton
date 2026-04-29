import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Conversation {
  id: number;
  botId: number;
  userId: number;
  title: string;
  status: string;
  messageCount: number;
  createdAt: string;
  updatedAt: string;
  closedAt?: string;
}

export interface ConversationMessage {
  id: number;
  conversationId: number;
  senderId: number;
  senderUsername: string;
  messageType: string;
  content: string;
  sentimentScore?: number;
  intent?: string;
  confidence?: number;
  responseTimeMs?: number;
  createdAt: string;
  isFlagged: boolean;
}

export interface ConversationExchange {
  userMessage: ConversationMessage;
  botMessage: ConversationMessage;
}

@Injectable({
  providedIn: 'root'
})
export class ConversationService {
  private apiBase = environment.apiUrl || '';
  private apiUrl = `${this.apiBase}/api/conversations`;
  private messageUrl = `${this.apiBase}/api/messages`;

  constructor(private http: HttpClient) {}

  createConversation(conversation: Conversation): Observable<Conversation> {
    return this.http.post<Conversation>(this.apiUrl, conversation);
  }

  getConversation(id: number): Observable<Conversation> {
    return this.http.get<Conversation>(`${this.apiUrl}/${id}`);
  }

  listByBot(botId: number, page = 0, size = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/bot/${botId}`, { params });
  }

  listByUser(userId: number, page = 0, size = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/user/${userId}`, { params });
  }

  listActiveByBot(botId: number, page = 0, size = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/bot/${botId}/active`, { params });
  }

  closeConversation(id: number): Observable<Conversation> {
    return this.http.patch<Conversation>(`${this.apiUrl}/${id}/close`, {});
  }

  updateTitle(id: number, title: string): Observable<Conversation> {
    const params = new HttpParams().set('title', title);
    return this.http.patch<Conversation>(`${this.apiUrl}/${id}/title`, {}, { params });
  }

  getActiveConversationCount(botId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/bot/${botId}/count`);
  }

  // Message operations
  addMessage(conversationId: number, message: ConversationMessage): Observable<ConversationMessage> {
    return this.http.post<ConversationMessage>(`${this.messageUrl}/conversation/${conversationId}`, message);
  }

  sendUserMessage(conversationId: number, message: ConversationMessage): Observable<ConversationExchange> {
    return this.http.post<ConversationExchange>(`${this.messageUrl}/conversation/${conversationId}/exchange`, message);
  }

  getMessage(id: number): Observable<ConversationMessage> {
    return this.http.get<ConversationMessage>(`${this.messageUrl}/${id}`);
  }

  listMessagesByConversation(conversationId: number, page = 0, size = 20): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.messageUrl}/conversation/${conversationId}`, { params });
  }

  getConversationHistory(conversationId: number): Observable<ConversationMessage[]> {
    return this.http.get<ConversationMessage[]>(`${this.messageUrl}/conversation/${conversationId}/history`);
  }

  flagMessage(id: number): Observable<ConversationMessage> {
    return this.http.patch<ConversationMessage>(`${this.messageUrl}/${id}/flag`, {});
  }

  listFlaggedMessages(botId: number, page = 0, size = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.messageUrl}/bot/${botId}/flagged`, { params });
  }

  getAverageResponseTime(botId: number): Observable<number> {
    return this.http.get<number>(`${this.messageUrl}/bot/${botId}/stats/avg-response-time`);
  }

  getAverageSentimentScore(botId: number): Observable<number> {
    return this.http.get<number>(`${this.messageUrl}/bot/${botId}/stats/avg-sentiment`);
  }
}
