package com.br.chatbotplatformskeleton.dto;

import java.time.OffsetDateTime;
import java.util.Set;

public class DepartmentDto {
    private Long id;
    private String name;
    private String description;
    private String code;
    private Long parentDepartmentId;
    private Long managerId;
    private String location;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Set<Long> userIds;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getParentDepartmentId() { return parentDepartmentId; }
    public void setParentDepartmentId(Long parentDepartmentId) { this.parentDepartmentId = parentDepartmentId; }

    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<Long> getUserIds() { return userIds; }
    public void setUserIds(Set<Long> userIds) { this.userIds = userIds; }
}

