import { Component, OnInit } from '@angular/core';
import { ComplianceSecurityService } from '../compliance-security.service';
import { ConsentLogDto } from '../models/compliance.model';

@Component({
    selector: 'app-consent-manager',
    templateUrl: './consent-manager.component.html',
    styles: [`
    .container { padding: 20px; }
    h2 { margin-bottom: 20px; color: #1976d2; }
    h3 { margin-top: 30px; margin-bottom: 15px; color: #333; }
    mat-card { margin-bottom: 20px; }
    .info-card { background-color: #e3f2fd; border-left: 4px solid #1976d2; }
    .loading { display: flex; justify-content: center; padding: 20px; }
    .consent-history { margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd; }
    mat-selection-list { width: 100%; max-height: 400px; overflow-y: auto; }
    ul { margin: 15px 0; padding-left: 20px; }
    li { margin: 10px 0; line-height: 1.6; }
    mat-list { padding: 0; }
  `],
    standalone: false
})
export class ConsentManagerComponent implements OnInit {
  consents: ConsentLogDto[] = [];
  loading = false;
  error: string | null = null;
  success: string | null = null;

  consentTypes = [
    { id: 'MARKETING', label: 'Marketing e Promoções', description: 'Receber emails de marketing e promoções' },
    { id: 'ANALYTICS', label: 'Analytics', description: 'Permitir coleta de dados para análise' },
    { id: 'PERSONAL_DATA', label: 'Dados Pessoais', description: 'Armazenar dados pessoais adicionais' },
    { id: 'GDPR', label: 'GDPR', description: 'Conformidade com GDPR' }
  ];

  constructor(private service: ComplianceSecurityService) {}

  ngOnInit(): void {
    this.loadConsents();
  }

  loadConsents(): void {
    this.loading = true;
    this.error = null;
    this.service.getMyConsents().subscribe({
      next: (data) => {
        this.consents = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar consentimentos: ' + err.message;
        this.loading = false;
      }
    });
  }

  isConsentGranted(type: string): boolean {
    const consent = this.consents.find(c => c.consentType === type);
    return consent?.status === 'GRANTED';
  }

  toggleConsent(type: string): void {
    if (this.isConsentGranted(type)) {
      this.revokeConsent(type);
    } else {
      this.grantConsent(type);
    }
  }

  grantConsent(type: string): void {
    this.service.grantConsent(type).subscribe({
      next: () => {
        this.success = `Consentimento para "${type}" concedido`;
        this.loadConsents();
        setTimeout(() => (this.success = null), 3000);
      },
      error: (err) => {
        this.error = 'Erro ao conceder consentimento: ' + err.message;
      }
    });
  }

  revokeConsent(type: string): void {
    if (confirm(`Tem certeza que deseja revogar consentimento de "${type}"?`)) {
      this.service.revokeConsent(type).subscribe({
        next: () => {
          this.success = `Consentimento para "${type}" revogado`;
          this.loadConsents();
          setTimeout(() => (this.success = null), 3000);
        },
        error: (err) => {
          this.error = 'Erro ao revogar consentimento: ' + err.message;
        }
      });
    }
  }

  getConsentLabel(type: string): string {
    const consent = this.consentTypes.find(c => c.id === type);
    return consent?.label || type;
  }

  getConsentDescription(type: string): string {
    const consent = this.consentTypes.find(c => c.id === type);
    return consent?.description || '';
  }

  compareConsent(c1: string, c2: string): boolean {
    return c1 && c2 ? c1 === c2 : c1 === c2;
  }
}

