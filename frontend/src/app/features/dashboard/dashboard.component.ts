import { ChangeDetectionStrategy, ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { timeout } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { DashboardService, DashboardStatsResponse } from './dashboard.service';
import { getApiErrorMessage } from '../../core/api-error.util';

export interface DashboardStats {
  botCount: number;
  activeConversationCount: number;
  totalMessageCount: number;
  userCount: number;
}

interface DashboardMetricCard {
  label: string;
  description: string;
  icon: string;
  tone: 'brand' | 'sky' | 'accent' | 'success';
  route: string;
  value: number;
}

interface QuickAction {
  label: string;
  description: string;
  icon: string;
  route: string;
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
    MatTableModule,
    RouterModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardComponent implements OnInit {
  readonly quickActions: QuickAction[] = [
    {
      label: 'Criar novo bot',
      description: 'Cadastre um bot com chave e configuracao inicial.',
      icon: 'add_circle',
      route: '/bots/create'
    },
    {
      label: 'Acompanhar conversas',
      description: 'Acesse fluxos em andamento e abra novas interacoes.',
      icon: 'forum',
      route: '/conversations'
    },
    {
      label: 'Abrir monitoramento',
      description: 'Veja a saude dos bots e indicadores de desempenho.',
      icon: 'monitoring',
      route: '/analytics-advanced/dashboard'
    },
    {
      label: 'Gerir perfis',
      description: 'Atualize papeis, acessos e ownership da operacao.',
      icon: 'badge',
      route: '/professional/users'
    }
  ];

  stats: DashboardStats = {
    botCount: 0,
    activeConversationCount: 0,
    totalMessageCount: 0,
    userCount: 0
  };
  loading = true;
  error = '';
  lastUpdated: Date | null = null;
  retryCount = 0;
  maxRetries = 3;

  private readonly destroyRef = inject(DestroyRef);
  private readonly cdr = inject(ChangeDetectorRef);

  constructor(
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = '';
    this.retryCount = 0;
    this.cdr.markForCheck();
    this.attemptLoad();
  }

  private attemptLoad(): void {
    this.dashboardService.getStats()
      .pipe(
        timeout(8000),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: (data: DashboardStatsResponse) => {
          this.stats = {
            botCount: data.botCount ?? 0,
            activeConversationCount: data.activeConversationCount ?? 0,
            totalMessageCount: data.totalMessageCount ?? 0,
            userCount: data.userCount ?? 0
          };
          this.lastUpdated = new Date();
          this.loading = false;
          this.error = '';
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erro ao carregar estatísticas do dashboard', err);

          // Se for 401 e ainda não tentou retry
          if (err?.status === 401 && this.retryCount < this.maxRetries) {
            this.retryCount++;
            console.log(`Tentativa ${this.retryCount}/${this.maxRetries} de carregar dados...`);
            setTimeout(() => this.attemptLoad(), 1000 * this.retryCount);
            return;
          }

          this.error = getApiErrorMessage(err, 'Nao foi possivel carregar as estatisticas do dashboard.');
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
  }

  get metricCards(): DashboardMetricCard[] {
    return [
      {
        label: 'Bots',
        description: 'Inventario ativo e disponivel para operacao.',
        icon: 'smart_toy',
        tone: 'brand',
        route: '/bots',
        value: this.stats.botCount
      },
      {
        label: 'Conversas ativas',
        description: 'Interacoes em andamento no momento.',
        icon: 'forum',
        tone: 'sky',
        route: '/conversations',
        value: this.stats.activeConversationCount
      },
      {
        label: 'Mensagens',
        description: 'Volume total processado pela plataforma.',
        icon: 'chat',
        tone: 'accent',
        route: '/analytics-advanced/dashboard',
        value: this.stats.totalMessageCount
      },
      {
        label: 'Usuarios',
        description: 'Operadores e stakeholders cadastrados.',
        icon: 'groups',
        tone: 'success',
        route: '/professional/users',
        value: this.stats.userCount
      }
    ];
  }

  get averageMessagesPerConversation(): string {
    if (this.stats.activeConversationCount === 0) {
      return '0';
    }

    return (this.stats.totalMessageCount / this.stats.activeConversationCount).toFixed(1);
  }

  get activityHeadline(): string {
    if (this.stats.botCount === 0) {
      return 'Configure o primeiro bot para iniciar a operacao.';
    }

    if (this.stats.activeConversationCount === 0) {
      return 'O ambiente esta pronto e sem conversas em andamento agora.';
    }

    return 'A operacao esta ativa e o monitoramento centralizado esta em dia.';
  }

  get lastUpdatedLabel(): string {
    return this.lastUpdated ? this.lastUpdated.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' }) : '--:--';
  }
}
