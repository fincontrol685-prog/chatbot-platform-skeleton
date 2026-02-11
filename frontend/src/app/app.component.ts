import { Component } from '@angular/core';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  template: `
    <mat-toolbar color="primary">
      <span style="cursor:pointer" routerLink="/">Chatbot Platform</span>
      <span class="spacer"></span>
      <a mat-button routerLink="/bots">Bots</a>
      <a mat-button routerLink="/bots/create">Create Bot</a>
      <a mat-button routerLink="/login" *ngIf="!auth.isAuthenticated()">Login</a>
      <button mat-button (click)="logout()" *ngIf="auth.isAuthenticated()">Logout</button>
    </mat-toolbar>
    <div style="padding:16px"><router-outlet></router-outlet></div>
  `,
  styles: [`.spacer { flex: 1 1 auto; }`]
})
export class AppComponent {
  constructor(public auth: AuthService) {}
  logout() { this.auth.logout(); }
}
