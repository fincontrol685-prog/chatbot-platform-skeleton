import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BotListComponent } from './bot-list/bot-list.component';
import { BotService } from './bot.service';
import { BotCreateComponent } from './bot-create/bot-create.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';

@NgModule({
  declarations: [BotListComponent, BotCreateComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MaterialModule, RouterModule.forChild([
    { path: '', component: BotListComponent },
    { path: 'create', component: BotCreateComponent },
    { path: ':id/edit', component: BotCreateComponent }
  ])]
})
export class BotsModule { }
