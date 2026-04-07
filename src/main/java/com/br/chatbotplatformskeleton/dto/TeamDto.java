package com.br.chatbotplatformskeleton.dto;

import java.time.OffsetDateTime;
import java.util.Set;

public class TeamDto {
    private Long id;
    private String name;
    private String description;
    private String code;
    private Long departmentId;
    private Long teamLeadId;
    private Boolean isActive;
    private Integer maxConversationsPerUser;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Set<Long> memberIds;
    private String departmentName;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getTeamLeadId() { return teamLeadId; }
    public void setTeamLeadId(Long teamLeadId) { this.teamLeadId = teamLeadId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getMaxConversationsPerUser() { return maxConversationsPerUser; }
    public void setMaxConversationsPerUser(Integer maxConversationsPerUser) { this.maxConversationsPerUser = maxConversationsPerUser; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<Long> getMemberIds() { return memberIds; }
    public void setMemberIds(Set<Long> memberIds) { this.memberIds = memberIds; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}

