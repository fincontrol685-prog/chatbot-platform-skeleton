package com.br.chatbotplatformskeleton.dto;

import java.time.OffsetDateTime;

public class ConsentLogDto {
    private Long id;
    private Long userId;
    private String consentType;
    private Boolean isGranted;
    private String consentVersion;
    private OffsetDateTime createdAt;
    private OffsetDateTime withdrawnAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getConsentType() { return consentType; }
    public void setConsentType(String consentType) { this.consentType = consentType; }

    public Boolean getIsGranted() { return isGranted; }
    public void setIsGranted(Boolean isGranted) { this.isGranted = isGranted; }

    public String getConsentVersion() { return consentVersion; }
    public void setConsentVersion(String consentVersion) { this.consentVersion = consentVersion; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getWithdrawnAt() { return withdrawnAt; }
    public void setWithdrawnAt(OffsetDateTime withdrawnAt) { this.withdrawnAt = withdrawnAt; }
}

