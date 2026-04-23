import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  tokenType: string;
}

interface JwtPayload {
  exp?: number;
  sub?: string;
  roles?: string[];
}

export interface AuthSession {
  isAuthenticated: boolean;
  username: string | null;
  displayName: string;
  initials: string;
  expiresAt: number | null;
  roles: string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiBase = environment.apiUrl || '';
  private readonly api = `${this.apiBase}/api/auth`;
  private readonly tokenKey = 'access_token';
  private readonly anonymousSession: AuthSession = {
    isAuthenticated: false,
    username: null,
    displayName: 'Visitante',
    initials: 'VS',
    expiresAt: null,
    roles: []
  };
  private readonly sessionSubject = new BehaviorSubject<AuthSession>(this.readSessionFromStorage());
  readonly session$ = this.sessionSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(username: string, password: string) {
    return this.http.post<AuthResponse>(`${this.api}/login`, { username, password }).pipe(
      tap(res => {
        this.persistToken(res?.accessToken);
      })
    );
  }

  register(username: string, email: string, password: string) {
    return this.http.post<AuthResponse>(`${this.api}/register`, { username, email, password }).pipe(
      tap(res => {
        this.persistToken(res?.accessToken);
      })
    );
  }

  requestPasswordReset(email: string) {
    return this.http.post<void>(`${this.api}/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string) {
    return this.http.post<void>(`${this.api}/reset-password`, { token, newPassword });
  }

  logout(redirectToLogin = false) {
    localStorage.removeItem(this.tokenKey);
    this.sessionSubject.next(this.anonymousSession);

    if (redirectToLogin && this.router.url !== '/login') {
      void this.router.navigate(['/login'], {
        queryParams: this.router.url && this.router.url !== '/login' ? { returnUrl: this.router.url } : undefined
      });
    }
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.tokenKey);

    if (!token) {
      this.sessionSubject.next(this.anonymousSession);
      return null;
    }

    const session = this.buildSession(token);

    if (!session.isAuthenticated) {
      this.logout();
      return null;
    }

    this.sessionSubject.next(session);
    return token;
  }

  isAuthenticated(): boolean {
    return this.sessionSubject.value.isAuthenticated || !!this.getToken();
  }

  handleUnauthorized(): void {
    this.logout(true);
  }

  getSessionSnapshot(): AuthSession {
    return this.sessionSubject.value;
  }

  private persistToken(token?: string | null): void {
    if (!token) {
      return;
    }

    localStorage.setItem(this.tokenKey, token);
    this.sessionSubject.next(this.buildSession(token));
  }

  private readSessionFromStorage(): AuthSession {
    return this.buildSession(localStorage.getItem(this.tokenKey));
  }

  private buildSession(token: string | null): AuthSession {
    const payload = this.decodeTokenPayload(token);

    if (!payload || typeof payload.exp !== 'number' || !payload.sub) {
      return this.anonymousSession;
    }

    const expiresAt = payload.exp * 1000;

    if (expiresAt <= Date.now()) {
      return this.anonymousSession;
    }

    const displayName = this.toDisplayName(payload.sub);

    return {
      isAuthenticated: true,
      username: payload.sub,
      displayName,
      initials: this.toInitials(displayName),
      expiresAt,
      roles: Array.isArray(payload.roles) ? payload.roles : []
    };
  }

  private decodeTokenPayload(token: string | null): JwtPayload | null {
    if (!token) {
      return null;
    }

    const parts = token.split('.');

    if (parts.length !== 3) {
      return null;
    }

    try {
      const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const padded = base64.padEnd(Math.ceil(base64.length / 4) * 4, '=');
      return JSON.parse(window.atob(padded));
    } catch {
      return null;
    }
  }

  private toDisplayName(identity: string): string {
    const rawLabel = identity.includes('@') ? identity.split('@')[0] : identity;
    const sanitizedLabel = rawLabel.replace(/[._-]+/g, ' ').trim();

    if (!sanitizedLabel) {
      return 'Operador';
    }

    return sanitizedLabel
      .split(' ')
      .filter(Boolean)
      .map(part => part.charAt(0).toUpperCase() + part.slice(1).toLowerCase())
      .join(' ');
  }

  private toInitials(label: string): string {
    const parts = label.split(' ').filter(Boolean);

    if (parts.length === 0) {
      return 'OP';
    }

    if (parts.length === 1) {
      return parts[0].slice(0, 2).toUpperCase();
    }

    return `${parts[0][0]}${parts[1][0]}`.toUpperCase();
  }
}
