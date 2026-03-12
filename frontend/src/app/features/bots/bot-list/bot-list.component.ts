import { Component, OnInit } from '@angular/core';
import { BotService, BotDto } from '../bot.service';

@Component({
  selector: 'app-bot-list',
  templateUrl: './bot-list.component.html'
})
export class BotListComponent implements OnInit {
  bots: BotDto[] = [];
  displayedColumns = ['name', 'key', 'enabled', 'actions'];
  filter = '';
  statusFilter: 'ALL' | 'ENABLED' | 'DISABLED' = 'ALL';

  constructor(private botService: BotService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.botService.list().subscribe({
      next: bots => (this.bots = bots),
      error: err => console.error('Erro ao carregar bots', err)
    });
  }

  get filteredBots(): BotDto[] {
    const text = this.filter?.toLowerCase().trim();
    return this.bots.filter(bot => {
      const matchesText = text
        ? (bot.name?.toLowerCase().includes(text) || bot.key?.toLowerCase().includes(text))
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

  toggle(bot: BotDto): void {
    this.botService
      .activate(bot.id, !bot.enabled)
      .subscribe({ next: () => this.load(), error: e => console.error('Erro ao atualizar bot', e) });
  }
}
