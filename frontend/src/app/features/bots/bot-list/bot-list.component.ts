import { ChangeDetectionStrategy, ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';
import { getApiErrorMessage } from '../../../core/api-error.util';
import { BotService, BotDto } from '../bot.service';
import { BotConfigSummary, buildBotConfigSummary } from '../bot-config.util';

interface BotListItem extends BotDto {
  summary: BotConfigSummary;
}

@Component({
  selector: 'app-bot-list',
  templateUrl: './bot-list.component.html',
  styleUrls: ['./bot-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BotListComponent implements OnInit {
  bots: BotListItem[] = [];
  displayedColumns = ['name', 'profile', 'messages', 'enabled', 'actions'];
  filter = '';
  statusFilter: 'ALL' | 'ENABLED' | 'DISABLED' = 'ALL';
  loading = true;
  error = '';

  private readonly destroyRef = inject(DestroyRef);
  private readonly cdr = inject(ChangeDetectorRef);

  constructor(
    private botService: BotService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.botService.list()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: bots => {
          this.bots = bots.map(bot => this.decorateBot(bot));
          this.loading = false;
          this.cdr.markForCheck();
        },
        error: err => {
          console.error('Erro ao carregar bots', err);
          this.error = getApiErrorMessage(err, 'Nao foi possivel carregar os bots.');
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
  }

  get filteredBots(): BotListItem[] {
    const text = this.filter?.toLowerCase().trim();
    return this.bots.filter(bot => {
      const matchesText = text
        ? [
            bot.name,
            bot.key,
            bot.summary.role,
            bot.summary.department,
            bot.summary.primaryChannelLabel,
            bot.summary.toneLabel
          ].some(value => value?.toLowerCase().includes(text))
        : true;
      const matchesStatus =
        this.statusFilter === 'ALL'
          ? true
          : this.statusFilter === 'ENABLED'
            ? bot.enabled
            : !bot.enabled;
      return matchesText && matchesStatus;
    });
  }

  get totalBots(): number {
    return this.bots.length;
  }

  get enabledBots(): number {
    return this.bots.filter(bot => bot.enabled).length;
  }

  get configuredChannels(): number {
    return new Set(
      this.bots
        .filter(bot => bot.summary.isStructured && bot.summary.primaryChannel)
        .map(bot => bot.summary.primaryChannel)
    ).size;
  }

  get messageReadyBots(): number {
    return this.bots.filter(bot => bot.summary.messagePackComplete).length;
  }

  toggle(bot: BotListItem): void {
    this.botService
      .activate(bot.id, !bot.enabled)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: updatedBot => {
          this.bots = this.bots.map(item => item.id === updatedBot.id ? this.decorateBot(updatedBot) : item);
          this.cdr.markForCheck();
          this.snackBar.open(
            `Bot ${updatedBot.name} ${updatedBot.enabled ? 'ativado' : 'desativado'} com sucesso.`,
            'Fechar',
            { duration: 3200 }
          );
        },
        error: e => {
          console.error('Erro ao atualizar bot', e);
          this.snackBar.open(getApiErrorMessage(e, 'Nao foi possivel atualizar o bot.'), 'Fechar', {
            duration: 3800
          });
        }
      });
  }

  private decorateBot(bot: BotDto): BotListItem {
    return {
      ...bot,
      summary: buildBotConfigSummary(bot.config)
    };
  }
}
