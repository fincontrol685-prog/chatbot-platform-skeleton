package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ANALYTICS_METRIC")
public class AnalyticsMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_type")
    private String metricType;  // CONVERSATION_COUNT, MESSAGE_COUNT, AVG_RESPONSE_TIME, SENTIMENT_SCORE, etc.

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "bot_id")
    private Long botId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "period_date")
    private java.time.LocalDate periodDate;

    @Column(name = "period_hour")
    private Integer periodHour;  // Para métricas por hora (0-23)

    @Column(name = "dimension_key")
    private String dimensionKey;  // Chave para filtros dimensionais

    @Column(name = "dimension_value")
    private String dimensionValue;

    @Column(name = "recorded_at")
    private OffsetDateTime recordedAt = OffsetDateTime.now();

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

    public java.time.LocalDate getPeriodDate() { return periodDate; }
    public void setPeriodDate(java.time.LocalDate periodDate) { this.periodDate = periodDate; }

    public Integer getPeriodHour() { return periodHour; }
    public void setPeriodHour(Integer periodHour) { this.periodHour = periodHour; }

    public String getDimensionKey() { return dimensionKey; }
    public void setDimensionKey(String dimensionKey) { this.dimensionKey = dimensionKey; }

    public String getDimensionValue() { return dimensionValue; }
    public void setDimensionValue(String dimensionValue) { this.dimensionValue = dimensionValue; }

    public OffsetDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(OffsetDateTime recordedAt) { this.recordedAt = recordedAt; }
}

