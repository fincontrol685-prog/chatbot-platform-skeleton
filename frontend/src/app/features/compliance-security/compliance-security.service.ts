import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  TwoFactorAuthDto,
  TwoFactorVerifyDto,
  ConsentLogDto,
  DataDeletionRequestDto,
  ComplianceStatusDto
} from './models/compliance.model';

@Injectable({ providedIn: 'root' })
export class ComplianceSecurityService {
  private apiBase = environment.apiUrl || '';
  private securityApi = `${this.apiBase}/api/security`;
  private complianceApi = `${this.apiBase}/api/compliance`;

  constructor(private http: HttpClient) {}

  // ===== 2FA SETUP =====
  initiateTwoFactorAuth(): Observable<TwoFactorAuthDto> {
    return this.http.post<TwoFactorAuthDto>(`${this.securityApi}/2fa/init`, {});
  }

  verifyAndActivateTwoFactorAuth(payload: TwoFactorVerifyDto): Observable<TwoFactorAuthDto> {
    return this.http.post<TwoFactorAuthDto>(`${this.securityApi}/2fa/verify`, payload);
  }

  validateTotpCode(code: string): Observable<{ valid: boolean }> {
    return this.http.post<{ valid: boolean }>(`${this.securityApi}/2fa/validate`, { code });
  }

  getTwoFactorStatus(): Observable<TwoFactorAuthDto> {
    return this.http.get<TwoFactorAuthDto>(`${this.securityApi}/2fa/status`);
  }

  disableTwoFactorAuth(): Observable<void> {
    return this.http.delete<void>(`${this.securityApi}/2fa`);
  }

  // ===== CONSENT (GDPR) =====
  grantConsent(consentType: string): Observable<ConsentLogDto> {
    return this.http.post<ConsentLogDto>(`${this.complianceApi}/consent`, { consentType });
  }

  revokeConsent(consentType: string): Observable<void> {
    return this.http.delete<void>(`${this.complianceApi}/consent/${consentType}`);
  }

  getMyConsents(): Observable<ConsentLogDto[]> {
    return this.http.get<ConsentLogDto[]>(`${this.complianceApi}/consent/my`);
  }

  // ===== DATA DELETION REQUESTS =====
  requestDataDeletion(): Observable<DataDeletionRequestDto> {
    return this.http.post<DataDeletionRequestDto>(
      `${this.complianceApi}/data-deletion/request`,
      {}
    );
  }

  getMyDeletionRequests(): Observable<DataDeletionRequestDto[]> {
    return this.http.get<DataDeletionRequestDto[]>(
      `${this.complianceApi}/data-deletion/my`
    );
  }

  getPendingDeletionRequests(): Observable<DataDeletionRequestDto[]> {
    return this.http.get<DataDeletionRequestDto[]>(
      `${this.complianceApi}/data-deletion/pending`
    );
  }

  approveDeletionRequest(id: number): Observable<DataDeletionRequestDto> {
    return this.http.put<DataDeletionRequestDto>(
      `${this.complianceApi}/data-deletion/${id}/approve`,
      {}
    );
  }

  rejectDeletionRequest(id: number, reason: string): Observable<DataDeletionRequestDto> {
    return this.http.put<DataDeletionRequestDto>(
      `${this.complianceApi}/data-deletion/${id}/reject`,
      { rejectionReason: reason }
    );
  }

  // ===== COMPLIANCE STATUS =====
  getComplianceStatus(): Observable<ComplianceStatusDto> {
    return this.http.get<ComplianceStatusDto>(`${this.complianceApi}/status`);
  }

  requestDataExport(): Observable<{ downloadUrl: string }> {
    return this.http.post<{ downloadUrl: string }>(
      `${this.complianceApi}/data-export`,
      {}
    );
  }
}

