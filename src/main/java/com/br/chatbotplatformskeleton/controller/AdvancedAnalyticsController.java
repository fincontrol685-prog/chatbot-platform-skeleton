package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.AnalyticsMetricDto;
import com.br.chatbotplatformskeleton.dto.CustomReportDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.AdvancedAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics-advanced")
@Tag(name = "Advanced Analytics", description = "Análise Avançada e Relatórios Customizados")
@SecurityRequirement(name = "Bearer Authentication")
public class AdvancedAnalyticsController {

    private final AdvancedAnalyticsService analyticsService;
    private final UserRepository userRepository;

    public AdvancedAnalyticsController(AdvancedAnalyticsService analyticsService, UserRepository userRepository) {
        this.analyticsService = analyticsService;
        this.userRepository = userRepository;
    }

    /**
     * Registra uma métrica de analytics
     */
    @PostMapping("/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Registrar Métrica", description = "Registra uma nova métrica de analytics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Métrica registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<AnalyticsMetricDto> recordMetric(@RequestBody AnalyticsMetricDto dto) {
        AnalyticsMetricDto recorded = analyticsService.recordMetric(dto);
        return ResponseEntity.created(URI.create("/api/analytics-advanced/metrics/" + recorded.getId())).body(recorded);
    }

    /**
     * Obtém métricas de um bot
     */
    @GetMapping("/bot/{botId}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Métricas do Bot", description = "Obtém métricas específicas de um bot em um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<AnalyticsMetricDto>> getBotMetrics(
            @PathVariable Long botId,
            @RequestParam String metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AnalyticsMetricDto> metrics = analyticsService.getMetricsForBot(botId, metricType, startDate, endDate);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Obtém métricas de uma equipe
     */
    @GetMapping("/team/{teamId}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Métricas da Equipe", description = "Obtém métricas específicas de uma equipe em um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<AnalyticsMetricDto>> getTeamMetrics(
            @PathVariable Long teamId,
            @RequestParam String metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AnalyticsMetricDto> metrics = analyticsService.getMetricsForTeam(teamId, metricType, startDate, endDate);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Obtém métricas de um departamento
     */
    @GetMapping("/department/{departmentId}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Métricas do Departamento", description = "Obtém métricas específicas de um departamento em um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<AnalyticsMetricDto>> getDepartmentMetrics(
            @PathVariable Long departmentId,
            @RequestParam String metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AnalyticsMetricDto> metrics = analyticsService.getMetricsForDepartment(departmentId, metricType, startDate, endDate);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Cria um relatório customizado
     */
    @PostMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Criar Relatório Customizado", description = "Cria um novo relatório customizado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relatório criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<CustomReportDto> createReport(@RequestBody CustomReportDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        CustomReportDto created = analyticsService.createCustomReport(dto, userId);
        return ResponseEntity.created(URI.create("/api/analytics-advanced/reports/" + created.getId())).body(created);
    }

    /**
     * Lista relatórios acessíveis ao usuário
     */
    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Relatórios Acessíveis", description = "Lista todos os relatórios que o usuário pode acessar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatórios obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<CustomReportDto>> listReports(Pageable pageable, Authentication authentication) {
        Long userId = extractUserId(authentication);
        Page<CustomReportDto> reports = analyticsService.listAccessibleReports(userId, pageable);
        return ResponseEntity.ok(reports);
    }

    /**
     * Lista meus relatórios
     */
    @GetMapping("/reports/my")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Meus Relatórios", description = "Lista todos os relatórios criados pelo usuário atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatórios obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<CustomReportDto>> listMyReports(Pageable pageable, Authentication authentication) {
        Long userId = extractUserId(authentication);
        Page<CustomReportDto> reports = analyticsService.listMyReports(userId, pageable);
        return ResponseEntity.ok(reports);
    }

    /**
     * Obtém um relatório específico
     */
    @GetMapping("/reports/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Relatório", description = "Retorna os detalhes de um relatório específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório encontrado"),
            @ApiResponse(responseCode = "404", description = "Relatório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<CustomReportDto> getReport(@PathVariable Long reportId) {
        return analyticsService.getReport(reportId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza um relatório
     */
    @PutMapping("/reports/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Atualizar Relatório", description = "Atualiza um relatório customizado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Relatório não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<CustomReportDto> updateReport(@PathVariable Long reportId, @RequestBody CustomReportDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        CustomReportDto updated = analyticsService.updateCustomReport(reportId, dto, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deleta um relatório
     */
    @DeleteMapping("/reports/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Deletar Relatório", description = "Remove um relatório customizado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relatório deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Relatório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId, Authentication authentication) {
        Long userId = extractUserId(authentication);
        analyticsService.deleteReport(reportId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exporta métricas para Excel
     */
    @PostMapping("/export/excel")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Exportar para Excel", description = "Exporta métricas em formato Excel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao gerar arquivo"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<byte[]> exportToExcel(
            @RequestBody List<AnalyticsMetricDto> metrics,
            @RequestParam(required = false) String reportName) {
        try {
            byte[] data = analyticsService.exportMetricsToExcel(metrics, reportName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", (reportName != null ? reportName : "report") + ".xlsx");
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Exporta métricas para CSV
     */
    @PostMapping("/export/csv")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Exportar para CSV", description = "Exporta métricas em formato CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo gerado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<byte[]> exportToCSV(
            @RequestBody List<AnalyticsMetricDto> metrics,
            @RequestParam(required = false) String reportName) {
        byte[] data = analyticsService.exportMetricsToCSV(metrics);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", (reportName != null ? reportName : "report") + ".csv");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
                .map(user -> user.getId())
                .orElse(null);
        }
        return null;
    }
}

