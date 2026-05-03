import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ComplianceSecurityService } from '../compliance-security.service';
import { TwoFactorAuthDto } from '../models/compliance.model';

@Component({
  selector: 'app-two-factor-setup',
  templateUrl: './two-factor-setup.component.html',
  styles: [`
    .container { padding: 20px; }
    h2 { margin-bottom: 20px; color: #1976d2; }
    .status-section, .setup-section, .verify-section { max-width: 600px; }
    mat-card { margin-bottom: 20px; }
    .loading { display: flex; justify-content: center; padding: 20px; }
    .qr-code-section { text-align: center; padding: 20px; }
    .qr-code { max-width: 300px; margin: 20px 0; border: 1px solid #ddd; padding: 10px; }
    .secret-section { margin: 20px 0; padding: 15px; background-color: #f5f5f5; border-radius: 4px; }
    .secret-code { display: block; padding: 10px; background-color: #fff; border: 1px solid #ddd; border-radius: 4px; font-family: monospace; word-break: break-all; margin-top: 10px; }
    .backup-codes { margin-top: 20px; padding: 15px; background-color: #fff3cd; border: 1px solid #ffc107; border-radius: 4px; }
    .backup-codes h4 { margin-top: 0; color: #856404; }
    .codes-list { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
    .backup-code { padding: 8px; background-color: #fff; border: 1px solid #ddd; border-radius: 3px; font-family: monospace; font-size: 12px; text-align: center; }
    .actions { margin-top: 20px; display: flex; gap: 10px; }
    .full-width { width: 100%; margin-bottom: 15px; }
    mat-list { margin: 10px 0; }
    ul { margin: 15px 0; padding-left: 20px; }
    li { margin: 8px 0; }
    p { margin: 10px 0; color: #666; }
  `]
})
export class TwoFactorSetupComponent implements OnInit {
  twoFactorStatus: TwoFactorAuthDto | null = null;
  setupForm: FormGroup;
  verifyForm: FormGroup;
  loading = false;
  error: string | null = null;
  success: string | null = null;
  setupStep: 'status' | 'init' | 'verify' = 'status';
  qrCodeUrl: string | null = null;
  secret: string | null = null;
  backupCodes: string[] = [];

  constructor(
    private fb: FormBuilder,
    private service: ComplianceSecurityService
  ) {
    this.setupForm = this.fb.group({});
    this.verifyForm = this.fb.group({
      token: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    this.loadStatus();
  }

  loadStatus(): void {
    this.loading = true;
    this.service.getTwoFactorStatus().subscribe({
      next: (status) => {
        this.twoFactorStatus = status;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar status 2FA: ' + err.message;
        this.loading = false;
      }
    });
  }

  initiateTwoFactor(): void {
    this.loading = true;
    this.error = null;
    this.service.initiateTwoFactorAuth().subscribe({
      next: (result) => {
        this.qrCodeUrl = result.qrCodeUrl || null;
        this.secret = result.secret || null;
        this.setupStep = 'verify';
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao iniciar 2FA: ' + err.message;
        this.loading = false;
      }
    });
  }

  verifyAndActivate(): void {
    if (this.verifyForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    const payload = {
      secret: this.secret || '',
      token: this.verifyForm.get('token')?.value
    };

    this.service.verifyAndActivateTwoFactorAuth(payload).subscribe({
      next: (result) => {
        this.backupCodes = result.backupCodes || [];
        this.success = '2FA ativado com sucesso! Guarde seus códigos de backup em local seguro.';
        this.setupStep = 'status';
        this.loadStatus();
        this.loading = false;
        this.verifyForm.reset();
      },
      error: (err) => {
        this.error = 'Erro ao ativar 2FA: ' + err.message;
        this.loading = false;
      }
    });
  }

  disableTwoFactor(): void {
    if (confirm('Tem certeza que deseja desativar autenticação em 2 etapas?')) {
      this.loading = true;
      this.service.disableTwoFactorAuth().subscribe({
        next: () => {
          this.success = '2FA desativado com sucesso';
          this.loadStatus();
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Erro ao desativar 2FA: ' + err.message;
          this.loading = false;
        }
      });
    }
  }

  cancel(): void {
    this.setupStep = 'status';
    this.verifyForm.reset();
    this.qrCodeUrl = null;
    this.secret = null;
  }
}

