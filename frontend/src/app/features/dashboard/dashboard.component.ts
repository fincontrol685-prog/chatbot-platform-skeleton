import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { DashboardService, DashboardStatsResponse } from './dashboard.service';

export interface DashboardStats {
  botCount: number;
  activeConversationCount: number;
  totalMessageCount: number;
  userCount: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatGridListModule,
    MatIconModule,
    MatButtonModule,
    MatTabsModule,
    MatProgressBarModule,
    MatChipsModule,
    MatTableModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats = {
    botCount: 0,
    activeConversationCount: 0,
    totalMessageCount: 0,
    userCount: 0
  };
  loading = true;

  constructor(
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.dashboardService.getStats().subscribe({
      next: (data: DashboardStatsResponse) => {
        this.stats = {
          botCount: data.botCount ?? 0,
          activeConversationCount: data.activeConversationCount ?? 0,
          totalMessageCount: data.totalMessageCount ?? 0,
          userCount: data.userCount ?? 0
        };
        this.loading = false;
      },
      error: (err) => {
        // eslint-disable-next-line no-console
        console.error('Erro ao carregar estatísticas do dashboard', err);
        this.loading = false;
      }
    });
  }

  getStatColor(stat: string): string {
    const colors: { [key: string]: string } = {
      'bots': '#1976d2',
      'conversations': '#388e3c',
      'messages': '#d32f2f',
      'users': '#f57c00'
    };
    return colors[stat] || '#1976d2';
  }
}

