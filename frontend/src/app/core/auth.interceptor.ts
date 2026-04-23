import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();
    const isAuthEndpoint = req.url.includes('/api/auth/');
    const request = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(request).pipe(
      catchError(err => {
        if (err?.status === 401 && !isAuthEndpoint) {
          this.auth.handleUnauthorized();
        }

        return throwError(() => err);
      })
    );
  }
}
