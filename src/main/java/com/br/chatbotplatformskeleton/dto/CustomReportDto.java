package com.br.chatbotplatformskeleton.dto;

import java.time.OffsetDateTime;

public class CustomReportDto {
    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private Long ownerId;
    private String ownerName;
    private Boolean isPublic;
    private String reportDefinition;
    private String metricTypes;
    private String filters;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

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

