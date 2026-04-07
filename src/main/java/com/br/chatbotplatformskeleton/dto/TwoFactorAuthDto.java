package com.br.chatbotplatformskeleton.dto;

import java.time.OffsetDateTime;

public class TwoFactorAuthDto {
    private Long id;
    private Long userId;
    private Boolean isEnabled;
    private String method;  // TOTP, SMS, EMAIL
    private String phoneNumber;
    private Boolean isVerified;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastVerifiedAt;
    private String qrCode;  // For TOTP setup

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(OffsetDateTime lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}

