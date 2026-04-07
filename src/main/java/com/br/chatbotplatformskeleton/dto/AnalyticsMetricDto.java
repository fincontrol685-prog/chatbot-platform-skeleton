package com.br.chatbotplatformskeleton.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class AnalyticsMetricDto {
    private Long id;
    private String metricType;
    private Double metricValue;
    private Long botId;
    private Long departmentId;
    private Long teamId;
    private Long userId;
    private LocalDate periodDate;
    private Integer periodHour;
    private String dimensionKey;
    private String dimensionValue;
    private OffsetDateTime recordedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }

    public Double getMetricValue() { return metricValue; }
    public void setMetricValue(Double metricValue) { this.metricValue = metricValue; }

    public Long getBotId() { return botId; }
    public void setBotId(Long botId) { this.botId = botId; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getPeriodDate() { return periodDate; }
    public void setPeriodDate(LocalDate periodDate) { this.periodDate = periodDate; }

    public Integer getPeriodHour() { return periodHour; }
    public void setPeriodHour(Integer periodHour) { this.periodHour = periodHour; }

    public String getDimensionKey() { return dimensionKey; }
    public void setDimensionKey(String dimensionKey) { this.dimensionKey = dimensionKey; }

    public String getDimensionValue() { return dimensionValue; }
    public void setDimensionValue(String dimensionValue) { this.dimensionValue = dimensionValue; }

    public OffsetDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(OffsetDateTime recordedAt) { this.recordedAt = recordedAt; }
}

