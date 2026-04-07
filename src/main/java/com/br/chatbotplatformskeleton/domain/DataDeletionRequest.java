package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "DATA_DELETION_REQUEST")
public class DataDeletionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Column(name = "status")
    private String status = "PENDING";  // PENDING, APPROVED, REJECTED, COMPLETED

    @Column(name = "reason")
    private String reason;

    @Column(name = "requested_at")
    private OffsetDateTime requestedAt = OffsetDateTime.now();

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "ip_address")
    private String ipAddress;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserAccount getUser() { return user; }
    public void setUser(UserAccount user) { this.user = user; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public OffsetDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(OffsetDateTime requestedAt) { this.requestedAt = requestedAt; }

    public OffsetDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(OffsetDateTime processedAt) { this.processedAt = processedAt; }

    public Long getProcessedBy() { return processedBy; }
    public void setProcessedBy(Long processedBy) { this.processedBy = processedBy; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}

