import { Component, OnInit } from '@angular/core';
import { BotService, BotDto } from '../bot.service';

@Component({
  selector: 'app-bot-list',
  templateUrl: './bot-list.component.html'
})
export class BotListComponent implements OnInit {
  bots: BotDto[] = [];
  displayedColumns = ['name', 'key', 'enabled', 'actions'];

  constructor(private botService: BotService) {}

  ngOnInit(): void { this.load(); }

  load() { this.botService.list().subscribe(b => this.bots = b); }

  toggle(bot: BotDto) { this.botService.activate(bot.id, !bot.enabled).subscribe({ next: (updated) => this.load(), error: (e) => console.error(e) }); }
}
