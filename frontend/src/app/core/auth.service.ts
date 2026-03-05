import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
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

  register(username: string, email: string, password: string) {
    return this.http.post<any>(`${this.api}/register`, { username, email, password }).pipe(
      tap(res => {
        if (res && res.accessToken) {
          localStorage.setItem(this.tokenKey, res.accessToken);
        }
      })
    );
  }

  requestPasswordReset(email: string) {
    return this.http.post<void>(`${this.api}/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string) {
    return this.http.post<void>(`${this.api}/reset-password`, { token, newPassword });
  }

  logout() { localStorage.removeItem(this.tokenKey); }

  getToken(): string | null { return localStorage.getItem(this.tokenKey); }

  isAuthenticated(): boolean { return !!this.getToken(); }
}
