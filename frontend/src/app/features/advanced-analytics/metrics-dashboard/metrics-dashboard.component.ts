import { Component, OnInit } from '@angular/core';
import { BotAnalytics, AnalyticsService } from '../../../core/analytics.service';
import { BotDto, BotService } from '../../bots/bot.service';
import { getApiErrorMessage } from '../../../core/api-error.util';

interface MonitoringCard {
  label: string;
  value: string;
  helper: string;
  icon: string;
}

@Component({
    selector: 'app-metrics-dashboard',
    templateUrl: './metrics-dashboard.component.html',
    styleUrls: ['./metrics-dashboard.component.css'],
    standalone: false
})
export class MetricsDashboardComponent implements OnInit {
  bots: BotDto[] = [];
  selectedBotId = 0;
  loadingBots = false;
  loadingMetrics = false;
  error = '';
  botAnalytics: BotAnalytics | null = null;

  constructor(
    private readonly botService: BotService,
    private readonly analyticsService: AnalyticsService
  ) {}

  ngOnInit(): void {
    this.loadAvailableBots();
  }

  get selectedBot(): BotDto | undefined {
    return this.bots.find(bot => bot.id === this.selectedBotId);
  }

  get monitoringCards(): MonitoringCard[] {
    return [
      {
        label: 'Conversas totais',
        value: this.formatNumber(this.botAnalytics?.totalConversations ?? 0),
        helper: 'Volume acumulado para o bot selecionado',
        icon: 'chat'
      },
      {
        label: 'Conversas ativas',
        value: this.formatNumber(this.botAnalytics?.activeConversations ?? 0),
        helper: 'Atendimentos em andamento agora',
        icon: 'forum'
      },
      {
        label: 'Mensagens',
        value: this.formatNumber(this.botAnalytics?.totalMessages ?? 0),
        helper: 'Mensagens registradas no historico',
        icon: 'mark_chat_unread'
      },
      {
        label: 'Tempo medio',
        value: `${(this.botAnalytics?.averageResponseTime ?? 0).toFixed(0)} ms`,
        helper: 'Media de resposta observada',
        icon: 'timer'
      }
    ];
  }

  get sentimentLabel(): string {
    const score = this.botAnalytics?.averageSentimentScore ?? 0;

    if (score >= 0.75) {
      return 'Experiencia positiva';
    }

    if (score >= 0.45) {
      return 'Experiencia neutra';
    }

    return 'Experiencia com atencao';
  }

  loadAvailableBots(): void {
    this.loadingBots = true;
    this.error = '';

    this.botService.list().subscribe({
      next: (bots) => {
        this.bots = bots.filter(bot => bot.enabled);
        this.loadingBots = false;

        if (this.bots.length === 0) {
          this.botAnalytics = null;
          return;
        }

        this.selectedBotId = this.bots[0].id;
        this.loadMonitoring();
      },
      error: (err) => {
        this.loadingBots = false;
        this.error = getApiErrorMessage(err, 'Nao foi possivel carregar os bots para monitoramento.');
      }
    });
  }

  onBotChange(botId: number): void {
    if (this.selectedBotId === botId) {
      return;
    }

    this.selectedBotId = botId;
    this.loadMonitoring();
  }

  loadMonitoring(): void {
    if (!this.selectedBotId) {
      this.botAnalytics = null;
      return;
    }

    this.loadingMetrics = true;
    this.error = '';

    this.analyticsService.getBotAnalytics(this.selectedBotId).subscribe({
      next: (analytics) => {
        this.botAnalytics = {
          totalConversations: analytics.totalConversations ?? 0,
          activeConversations: analytics.activeConversations ?? 0,
          totalMessages: analytics.totalMessages ?? 0,
          averageResponseTime: analytics.averageResponseTime ?? 0,
          averageSentimentScore: analytics.averageSentimentScore ?? 0,
          messageVolumeByDay: analytics.messageVolumeByDay ?? []
        };
        this.loadingMetrics = false;
      },
      error: (err) => {
        this.botAnalytics = null;
        this.loadingMetrics = false;
        this.error = getApiErrorMessage(err, 'Nao foi possivel carregar o monitoramento deste bot.');
      }
    });
  }

  private formatNumber(value: number): string {
    return new Intl.NumberFormat('pt-BR').format(value);
  }
}
