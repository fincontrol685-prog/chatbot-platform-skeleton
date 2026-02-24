import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { RouterModule } from '@angular/router';
import { ConversationService, Conversation } from '../conversation.service';
import { ActivatedRoute } from '@angular/router';

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
  botId: number = 0;

  constructor(
    private conversationService: ConversationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['botId']) {
        this.botId = params['botId'];
        this.loadConversations();
      }
    });
  }

  loadConversations(): void {
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

