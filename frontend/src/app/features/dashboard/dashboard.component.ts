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
import { BotService } from '../bots/bot.service';
import { ConversationService } from '../conversations/conversation.service';

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
    private botService: BotService,
    private conversationService: ConversationService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    // Load bots count
    this.botService.list().subscribe(
      (data: any) => {
        this.stats.botCount = data.length || 0;
      }
    );

    // Load conversations count
    this.conversationService.listByBot(1, 0, 1000).subscribe(
      (data: any) => {
        this.stats.activeConversationCount = data.totalElements || 0;
      }
    );

    this.loading = false;
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

