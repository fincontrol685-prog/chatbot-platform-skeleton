import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BotService } from '../bot.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-bot-create',
  template: `
    <div style="max-width:800px;margin:20px auto;">
      <mat-card>
        <mat-card-title>Create Bot</mat-card-title>
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <mat-form-field appearance="fill" style="width:100%">
              <mat-label>Name</mat-label>
              <input matInput formControlName="name" />
              <mat-error *ngIf="form.get('name')?.hasError('required')">Name is required</mat-error>
            </mat-form-field>

            <mat-form-field appearance="fill" style="width:100%">
              <mat-label>Key</mat-label>
              <input matInput formControlName="key" />
              <mat-error *ngIf="form.get('key')?.hasError('required')">Key is required</mat-error>
            </mat-form-field>

            <mat-form-field appearance="fill" style="width:100%">
              <mat-label>Config (JSON)</mat-label>
              <textarea matInput formControlName="config" rows="6"></textarea>
            </mat-form-field>

            <div style="margin-top:16px">
              <button mat-raised-button color="primary" type="submit">Create</button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `
})
export class BotCreateComponent {
  form: FormGroup;

  constructor(private botService: BotService, private router: Router, fb: FormBuilder) {
    this.form = fb.group({ name: ['', Validators.required], key: ['', Validators.required], config: [''] });
  }

  submit() {
    if (this.form.invalid) return;
    this.botService.create(this.form.value).subscribe({ next: () => this.router.navigate(['/bots']), error: (e) => console.error(e) });
  }
}
