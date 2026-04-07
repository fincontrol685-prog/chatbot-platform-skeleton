package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "CUSTOM_REPORT")
public class CustomReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount owner;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(columnDefinition = "LONGTEXT")
    private String reportDefinition;  // JSON com a definição do relatório

    @Column(name = "metric_types")
    private String metricTypes;  // Comma-separated list

    @Column(name = "filters")
    private String filters;  // JSON com filtros aplicados

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public UserAccount getOwner() { return owner; }
    public void setOwner(UserAccount owner) { this.owner = owner; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public String getReportDefinition() { return reportDefinition; }
    public void setReportDefinition(String reportDefinition) { this.reportDefinition = reportDefinition; }

    public String getMetricTypes() { return metricTypes; }
    public void setMetricTypes(String metricTypes) { this.metricTypes = metricTypes; }

    public String getFilters() { return filters; }
    public void setFilters(String filters) { this.filters = filters; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}

