import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { Router, RouterModule } from '@angular/router';
import { ConversationService, Conversation } from '../conversation.service';
import { BotService, BotDto } from '../../bots/bot.service';

@Component({
  selector: 'app-conversation-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    RouterModule
  ],
  templateUrl: './conversation-list.component.html',
  styleUrls: ['./conversation-list.component.css']
})
export class ConversationListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'title', 'status', 'messageCount', 'createdAt', 'actions'];
  conversations: Conversation[] = [];
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  botId = 0;
  bots: BotDto[] = [];

  constructor(
    private conversationService: ConversationService,
    private botService: BotService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.botService.list().subscribe({
      next: bots => {
        this.bots = bots;
        if (this.bots.length > 0) {
          this.botId = this.bots[0].id;
          this.loadConversations();
        }
      },
      error: err => console.error('Erro ao carregar bots para conversas', err)
    });
  }

  loadConversations(): void {
    if (!this.botId) {
      this.conversations = [];
      this.totalElements = 0;
      return;
    }

    this.conversationService.listByBot(this.botId, this.currentPage, this.pageSize)
      .subscribe(data => {
        this.conversations = data.content;
        this.totalElements = data.totalElements;
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadConversations();
  }

  novaConversa(): void {
    if (!this.botId) {
      return;
    }
    const nova: Conversation = {
      id: 0,
      botId: this.botId,
      userId: 0,
      title: '',
      status: 'ACTIVE',
      messageCount: 0,
      createdAt: '',
      updatedAt: ''
    };
    this.conversationService.createConversation(nova).subscribe({
      next: conv => this.router.navigate(['/conversations', conv.id]),
      error: err => console.error('Erro ao criar conversa', err)
    });
  }

  closeConversation(id: number): void {
    if (confirm('Tem certeza que deseja encerrar esta conversa?')) {
      this.conversationService.closeConversation(id).subscribe(
        () => this.loadConversations()
      );
    }
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'accent';
      case 'CLOSED':
        return 'warn';
      default:
        return 'primary';
    }
  }
}

