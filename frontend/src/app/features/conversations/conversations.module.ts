import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ConversationListComponent } from './conversation-list/conversation-list.component';
import { ConversationDetailComponent } from './conversation-detail/conversation-detail.component';

const routes: Routes = [
  {
    path: '',
    component: ConversationListComponent,
    data: {
      title: 'Central de conversas',
      description: 'Supervisione interacoes em andamento e inicie novos atendimentos.'
    }
  },
  {
    path: ':id',
    component: ConversationDetailComponent,
    data: {
      title: 'Conversa ativa',
      description: 'Acompanhe mensagens, responda usuarios e conclua o atendimento.'
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class ConversationsModule { }
