import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable()
export class AuthService {
  private apiBase = environment.apiUrl || '';
  private api = `${this.apiBase}/api/auth`;
  private tokenKey = 'access_token';

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    return this.http.post<any>(`${this.api}/login`, { username, password }).pipe(
      tap(res => {
        if (res && res.accessToken) {
          localStorage.setItem(this.tokenKey, res.accessToken);
        }
      })
    );
  }

  logout() { localStorage.removeItem(this.tokenKey); }

  getToken(): string | null { return localStorage.getItem(this.tokenKey); }

  isAuthenticated(): boolean { return !!this.getToken(); }
}
