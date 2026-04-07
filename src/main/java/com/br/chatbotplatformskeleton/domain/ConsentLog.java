package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "CONSENT_LOG")
public class ConsentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Column(name = "consent_type")
    private String consentType;  // DATA_PROCESSING, MARKETING, COOKIES, etc.

    @Column(name = "is_granted")
    private Boolean isGranted;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "consent_version")
    private String consentVersion;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "withdrawn_at")
    private OffsetDateTime withdrawnAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserAccount getUser() { return user; }
    public void setUser(UserAccount user) { this.user = user; }

    public String getConsentType() { return consentType; }
    public void setConsentType(String consentType) { this.consentType = consentType; }

    public Boolean getIsGranted() { return isGranted; }
    public void setIsGranted(Boolean isGranted) { this.isGranted = isGranted; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getConsentVersion() { return consentVersion; }
    public void setConsentVersion(String consentVersion) { this.consentVersion = consentVersion; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getWithdrawnAt() { return withdrawnAt; }
    public void setWithdrawnAt(OffsetDateTime withdrawnAt) { this.withdrawnAt = withdrawnAt; }
}

