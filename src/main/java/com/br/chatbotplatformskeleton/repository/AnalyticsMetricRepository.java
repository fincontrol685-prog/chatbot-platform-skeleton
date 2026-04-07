package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.AnalyticsMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnalyticsMetricRepository extends JpaRepository<AnalyticsMetric, Long> {

    List<AnalyticsMetric> findByMetricType(String metricType);

    List<AnalyticsMetric> findByBotIdAndPeriodDate(Long botId, LocalDate periodDate);

    @Query("SELECT am FROM AnalyticsMetric am WHERE am.botId = :botId AND am.metricType = :metricType AND am.periodDate BETWEEN :startDate AND :endDate ORDER BY am.periodDate ASC")
    List<AnalyticsMetric> findMetricsForBot(@Param("botId") Long botId, @Param("metricType") String metricType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT am FROM AnalyticsMetric am WHERE am.teamId = :teamId AND am.metricType = :metricType AND am.periodDate BETWEEN :startDate AND :endDate ORDER BY am.periodDate ASC")
    List<AnalyticsMetric> findMetricsForTeam(@Param("teamId") Long teamId, @Param("metricType") String metricType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT am FROM AnalyticsMetric am WHERE am.departmentId = :departmentId AND am.metricType = :metricType AND am.periodDate BETWEEN :startDate AND :endDate ORDER BY am.periodDate ASC")
    List<AnalyticsMetric> findMetricsForDepartment(@Param("departmentId") Long departmentId, @Param("metricType") String metricType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(am.metricValue) FROM AnalyticsMetric am WHERE am.botId = :botId AND am.metricType = :metricType AND am.periodDate = :date")
    Double sumMetricForBotOnDate(@Param("botId") Long botId, @Param("metricType") String metricType, @Param("date") LocalDate date);

    @Query("SELECT AVG(am.metricValue) FROM AnalyticsMetric am WHERE am.botId = :botId AND am.metricType = :metricType AND am.periodDate BETWEEN :startDate AND :endDate")
    Double avgMetricForBotBetweenDates(@Param("botId") Long botId, @Param("metricType") String metricType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

