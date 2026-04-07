export interface TwoFactorAuthDto {
  id?: number;
  userId?: number;
  isEnabled: boolean;
  method: '2FA_TOTP';
  isVerified: boolean;
  backupCodes?: string[];
  qrCodeUrl?: string;
  secret?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface TwoFactorVerifyDto {
  userId?: number;
  secret?: string;
  token: string;
}

export interface ConsentLogDto {
  id?: number;
  userId?: number;
  consentType: 'MARKETING' | 'ANALYTICS' | 'PERSONAL_DATA' | 'GDPR';
  status: 'GRANTED' | 'REVOKED';
  ipAddress?: string;
  userAgent?: string;
  grantedAt?: string;
  revokedAt?: string;
}

export interface DataDeletionRequestDto {
  id?: number;
  userId?: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'COMPLETED';
  requestedAt?: string;
  approvalDate?: string;
  rejectionReason?: string;
  approvedBy?: number;
  completedAt?: string;
}

export interface ComplianceStatusDto {
  twoFactorEnabled: boolean;
  gdprConsents?: ConsentLogDto[];
  deletionRequests?: DataDeletionRequestDto[];
  dataExportUrl?: string;
}

