package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.AnalyticsMetric;
import com.br.chatbotplatformskeleton.domain.CustomReport;
import com.br.chatbotplatformskeleton.dto.AnalyticsMetricDto;
import com.br.chatbotplatformskeleton.dto.CustomReportDto;
import com.br.chatbotplatformskeleton.repository.AnalyticsMetricRepository;
import com.br.chatbotplatformskeleton.repository.CustomReportRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.util.ExcelExportUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdvancedAnalyticsService {

    private final AnalyticsMetricRepository metricsRepository;
    private final CustomReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public AdvancedAnalyticsService(AnalyticsMetricRepository metricsRepository,
                                   CustomReportRepository reportRepository,
                                   UserRepository userRepository,
                                   AuditService auditService) {
        this.metricsRepository = metricsRepository;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    /**
     * Registra uma métrica de analytics
     */
    public AnalyticsMetricDto recordMetric(AnalyticsMetricDto dto) {
        AnalyticsMetric metric = new AnalyticsMetric();
        metric.setMetricType(dto.getMetricType());
        metric.setMetricValue(dto.getMetricValue());
        metric.setBotId(dto.getBotId());
        metric.setDepartmentId(dto.getDepartmentId());
        metric.setTeamId(dto.getTeamId());
        metric.setUserId(dto.getUserId());
        metric.setPeriodDate(dto.getPeriodDate() != null ? dto.getPeriodDate() : LocalDate.now());
        metric.setPeriodHour(dto.getPeriodHour());
        metric.setDimensionKey(dto.getDimensionKey());
        metric.setDimensionValue(dto.getDimensionValue());
        metric.setRecordedAt(OffsetDateTime.now());

        AnalyticsMetric saved = metricsRepository.save(metric);
        return toDto(saved);
    }

    /**
     * Obtém métricas para um bot em um período
     */
    public List<AnalyticsMetricDto> getMetricsForBot(Long botId, String metricType, LocalDate startDate, LocalDate endDate) {
        return metricsRepository.findMetricsForBot(botId, metricType, startDate, endDate)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Obtém métricas para uma equipe em um período
     */
    public List<AnalyticsMetricDto> getMetricsForTeam(Long teamId, String metricType, LocalDate startDate, LocalDate endDate) {
        return metricsRepository.findMetricsForTeam(teamId, metricType, startDate, endDate)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Obtém métricas para um departamento em um período
     */
    public List<AnalyticsMetricDto> getMetricsForDepartment(Long departmentId, String metricType, LocalDate startDate, LocalDate endDate) {
        return metricsRepository.findMetricsForDepartment(departmentId, metricType, startDate, endDate)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Cria um relatório customizado
     */
    public CustomReportDto createCustomReport(CustomReportDto dto, Long userId) {
        return userRepository.findById(userId).map(user -> {
            CustomReport report = new CustomReport();
            report.setName(dto.getName());
            report.setDescription(dto.getDescription());
            report.setCreatedBy(userId);
            report.setOwner(user);
            report.setIsPublic(dto.getIsPublic() != null ? dto.getIsPublic() : false);
            report.setReportDefinition(dto.getReportDefinition());
            report.setMetricTypes(dto.getMetricTypes());
            report.setFilters(dto.getFilters());
            report.setCreatedAt(OffsetDateTime.now());
            report.setUpdatedAt(OffsetDateTime.now());

            CustomReport saved = reportRepository.save(report);
            auditService.log(userId, "CREATE", "REPORT", saved.getId(), null, saved.getName());
            return toReportDto(saved);
        }).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    /**
     * Atualiza um relatório customizado
     */
    public CustomReportDto updateCustomReport(Long reportId, CustomReportDto dto, Long userId) {
        Optional<CustomReport> reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isEmpty()) {
            throw new IllegalArgumentException("Report not found: " + reportId);
        }

        CustomReport report = reportOpt.get();
        if (!report.getCreatedBy().equals(userId) && !report.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to update this report");
        }

        report.setName(dto.getName());
        report.setDescription(dto.getDescription());
        report.setIsPublic(dto.getIsPublic());
        report.setReportDefinition(dto.getReportDefinition());
        report.setMetricTypes(dto.getMetricTypes());
        report.setFilters(dto.getFilters());
        report.setUpdatedAt(OffsetDateTime.now());

        CustomReport saved = reportRepository.save(report);
        auditService.log(userId, "UPDATE", "REPORT", saved.getId(), null, saved.getName());
        return toReportDto(saved);
    }

    /**
     * Lista relatórios acessíveis ao usuário
     */
    public Page<CustomReportDto> listAccessibleReports(Long userId, Pageable pageable) {
        return reportRepository.findAccessibleReports(userId, pageable).map(this::toReportDto);
    }

    /**
     * Lista relatórios do usuário
     */
    public Page<CustomReportDto> listMyReports(Long userId, Pageable pageable) {
        return reportRepository.findByOwnerId(userId, pageable).map(this::toReportDto);
    }

    /**
     * Obtém um relatório específico
     */
    public Optional<CustomReportDto> getReport(Long reportId) {
        return reportRepository.findById(reportId).map(this::toReportDto);
    }

    /**
     * Exporta métricas para Excel
     */
    public byte[] exportMetricsToExcel(List<AnalyticsMetricDto> metrics, String reportName) throws Exception {
        return ExcelExportUtil.exportMetricsToExcel(metrics, reportName);
    }

    /**
     * Exporta métricas para CSV
     */
    public byte[] exportMetricsToCSV(List<AnalyticsMetricDto> metrics) {
        return ExcelExportUtil.exportMetricsToCSV(metrics);
    }

    /**
     * Deleta um relatório
     */
    public void deleteReport(Long reportId, Long userId) {
        Optional<CustomReport> reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isPresent()) {
            CustomReport report = reportOpt.get();
            if (report.getCreatedBy().equals(userId) || report.getOwner().getId().equals(userId)) {
                reportRepository.delete(report);
                auditService.log(userId, "DELETE", "REPORT", reportId, null, "deleted");
            }
        }
    }

    private AnalyticsMetricDto toDto(AnalyticsMetric metric) {
        AnalyticsMetricDto dto = new AnalyticsMetricDto();
        dto.setId(metric.getId());
        dto.setMetricType(metric.getMetricType());
        dto.setMetricValue(metric.getMetricValue());
        dto.setBotId(metric.getBotId());
        dto.setDepartmentId(metric.getDepartmentId());
        dto.setTeamId(metric.getTeamId());
        dto.setUserId(metric.getUserId());
        dto.setPeriodDate(metric.getPeriodDate());
        dto.setPeriodHour(metric.getPeriodHour());
        dto.setDimensionKey(metric.getDimensionKey());
        dto.setDimensionValue(metric.getDimensionValue());
        dto.setRecordedAt(metric.getRecordedAt());
        return dto;
    }

    private CustomReportDto toReportDto(CustomReport report) {
        CustomReportDto dto = new CustomReportDto();
        dto.setId(report.getId());
        dto.setName(report.getName());
        dto.setDescription(report.getDescription());
        dto.setCreatedBy(report.getCreatedBy());
        dto.setOwnerId(report.getOwner().getId());
        dto.setOwnerName(report.getOwner().getUsername());
        dto.setIsPublic(report.getIsPublic());
        dto.setReportDefinition(report.getReportDefinition());
        dto.setMetricTypes(report.getMetricTypes());
        dto.setFilters(report.getFilters());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        return dto;
    }
}

