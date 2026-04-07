package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TWO_FACTOR_AUTH")
public class TwoFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    @Column(name = "is_enabled")
    private Boolean isEnabled = false;

    @Column(name = "secret_key")
    private String secretKey;  // Encrypted TOTP secret

    @Column(name = "backup_codes")
    private String backupCodes;  // Encrypted comma-separated backup codes

    @Column(name = "backup_codes_used")
    private String backupCodesUsed;  // Encrypted comma-separated used codes

    @Column(name = "method")
    private String method = "TOTP";  // TOTP, SMS, EMAIL

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "last_verified_at")
    private OffsetDateTime lastVerifiedAt;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserAccount getUser() { return user; }
    public void setUser(UserAccount user) { this.user = user; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getBackupCodes() { return backupCodes; }
    public void setBackupCodes(String backupCodes) { this.backupCodes = backupCodes; }

    public String getBackupCodesUsed() { return backupCodesUsed; }
    public void setBackupCodesUsed(String backupCodesUsed) { this.backupCodesUsed = backupCodesUsed; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(OffsetDateTime lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
}

