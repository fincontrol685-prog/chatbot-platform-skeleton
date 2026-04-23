import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ConversationService, Conversation, ConversationMessage } from '../conversation.service';
import { getApiErrorMessage } from '../../../core/api-error.util';

@Component({
  selector: 'app-conversation-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatProgressBarModule,
    MatChipsModule,
    MatDividerModule,
    MatTooltipModule,
    RouterModule
  ],
  templateUrl: './conversation-detail.component.html',
  styleUrls: ['./conversation-detail.component.css']
})
export class ConversationDetailComponent implements OnInit {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;

  conversation: Conversation | null = null;
  messages: ConversationMessage[] = [];
  messageForm: FormGroup;
  loading = false;
  loadingConversation = true;
  conversationId: number = 0;
  error = '';

  constructor(
    private conversationService: ConversationService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.messageForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.conversationId = Number(params['id']);
        this.error = '';
        this.loadConversation();
        this.loadMessages();
      }
    });
  }

  loadConversation(): void {
    this.loadingConversation = true;
    this.conversationService.getConversation(this.conversationId)
      .subscribe({
        next: data => {
          this.conversation = data;
          this.loadingConversation = false;
        },
        error: err => {
          this.error = getApiErrorMessage(err, 'Nao foi possivel abrir a conversa.');
          this.loadingConversation = false;
        }
      });
  }

  loadMessages(): void {
    this.conversationService.getConversationHistory(this.conversationId)
      .subscribe({
        next: data => {
          this.messages = data;
          this.scrollToBottom();
        },
        error: err => {
          this.error = getApiErrorMessage(err, 'Nao foi possivel carregar as mensagens da conversa.');
        }
      });
  }

  sendMessage(): void {
    if (this.messageForm.invalid || !this.conversation) return;

    const newMessage: ConversationMessage = {
      id: 0,
      conversationId: this.conversationId,
      senderId: 0,
      senderUsername: 'Você',
      messageType: 'USER',
      content: this.messageForm.get('content')?.value,
      createdAt: new Date().toISOString(),
      isFlagged: false
    };

    this.loading = true;
    this.conversationService.addMessage(this.conversationId, newMessage)
      .subscribe(
        (message) => {
          this.messages.push(message);
          this.messageForm.reset();
          this.loading = false;
          this.scrollToBottom();

          // Simulate bot response
          this.simulateBotResponse();
        },
        (error) => {
          console.error('Erro ao enviar mensagem:', error);
          this.error = getApiErrorMessage(error, 'Nao foi possivel enviar a mensagem.');
          this.loading = false;
        }
      );
  }

  private simulateBotResponse(): void {
    setTimeout(() => {
      const botMessage: ConversationMessage = {
        id: 0,
        conversationId: this.conversationId,
        senderId: 0,
        senderUsername: 'Bot',
        messageType: 'BOT',
        content: 'Obrigado pela sua mensagem. Estou processando sua solicitação.',
        createdAt: new Date().toISOString(),
        isFlagged: false
      };
      this.messages.push(botMessage);
      this.scrollToBottom();
    }, 1000);
  }

  closeConversation(): void {
    if (confirm('Tem certeza que deseja encerrar esta conversa?')) {
      this.conversationService.closeConversation(this.conversationId)
        .subscribe(() => {
          this.loadConversation();
        });
    }
  }

  flagMessage(message: ConversationMessage): void {
    if (message.id > 0) {
      this.conversationService.flagMessage(message.id)
        .subscribe(() => {
          message.isFlagged = true;
        });
    }
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
      }
    }, 0);
  }

  getMessageClass(message: ConversationMessage): string {
    return message.messageType === 'USER' ? 'user-message' : 'bot-message';
  }
}
