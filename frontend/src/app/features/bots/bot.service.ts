import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface BotDto {
  id: number;
  name: string;
  key: string;
  enabled: boolean;
  config?: string | null;
}

@Injectable({ providedIn: 'root' })
export class BotService {
  private apiBase = environment.apiUrl || '';
  private api = `${this.apiBase}/api/bots`;
  constructor(private http: HttpClient) {}

  list(): Observable<BotDto[]> { return this.http.get<BotDto[]>(this.api); }
  get(id: number): Observable<BotDto> { return this.http.get<BotDto>(`${this.api}/${id}`); }
  create(bot: Partial<BotDto>) { return this.http.post<BotDto>(this.api, bot); }
  update(id: number, bot: Partial<BotDto>) { return this.http.put<BotDto>(`${this.api}/${id}`, bot); }
  activate(id: number, active: boolean) { return this.http.patch<BotDto>(`${this.api}/${id}/activate?active=${active}`, {}); }
}
