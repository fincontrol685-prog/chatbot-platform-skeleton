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
import { getApiErrorMessage } from '../../../core/api-error.util';

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
  loadingBots = false;
  creatingConversation = false;
  error = '';

  constructor(
    private conversationService: ConversationService,
    private botService: BotService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadingBots = true;
    this.botService.list().subscribe({
      next: bots => {
        this.bots = bots.filter(bot => bot.enabled);
        if (this.bots.length > 0) {
          this.botId = this.bots[0].id;
          this.loadConversations();
        } else {
          this.conversations = [];
          this.totalElements = 0;
        }
        this.loadingBots = false;
      },
      error: err => {
        console.error('Erro ao carregar bots para conversas', err);
        this.error = getApiErrorMessage(err, 'Nao foi possivel carregar os bots ativos.');
        this.loadingBots = false;
      }
    });
  }

  get selectedBot(): BotDto | undefined {
    return this.bots.find(bot => bot.id === this.botId);
  }

  loadConversations(): void {
    if (!this.botId) {
      this.conversations = [];
      this.totalElements = 0;
      return;
    }

    this.conversationService.listByBot(this.botId, this.currentPage, this.pageSize)
      .subscribe({
        next: data => {
          this.conversations = data.content;
          this.totalElements = data.totalElements;
        },
        error: err => {
          this.error = getApiErrorMessage(err, 'Nao foi possivel carregar as conversas do bot selecionado.');
          this.conversations = [];
          this.totalElements = 0;
        }
      });
  }

  selectBot(botId: number): void {
    if (this.botId === botId) {
      return;
    }

    this.botId = botId;
    this.currentPage = 0;
    this.loadConversations();
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadConversations();
  }

  novaConversa(): void {
    if (!this.botId || this.creatingConversation) {
      return;
    }

    this.creatingConversation = true;
    this.error = '';

    const nova: Conversation = {
      id: 0,
      botId: this.botId,
      userId: 0,
      title: this.selectedBot ? `Conversa com ${this.selectedBot.name}` : '',
      status: 'ACTIVE',
      messageCount: 0,
      createdAt: '',
      updatedAt: ''
    };
    this.conversationService.createConversation(nova).subscribe({
      next: conv => {
        this.creatingConversation = false;
        if (!conv?.id) {
          this.error = 'A conversa foi criada, mas nao retornou um identificador valido.';
          this.loadConversations();
          return;
        }
        void this.router.navigate(['/conversations', conv.id]);
      },
      error: err => {
        console.error('Erro ao criar conversa', err);
        this.error = getApiErrorMessage(err, 'Nao foi possivel criar a conversa.');
        this.creatingConversation = false;
      }
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
