package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Análise e Relatórios de Dados")
@SecurityRequirement(name = "Bearer Authentication")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Estatísticas do Dashboard", description = "Retorna as estatísticas gerais do dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }


    @GetMapping("/bots/{botId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Análise de Bot", description = "Retorna análise completa de um bot específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Map<String, Object>> getBotAnalytics(@PathVariable Long botId) {
        return ResponseEntity.ok(analyticsService.getBotAnalytics(botId));
    }

    @GetMapping("/bots/{botId}/sentiment")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Análise de Sentimento", description = "Retorna análise de sentimento das conversas de um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Map<String, Object>> getSentimentAnalysis(@PathVariable Long botId) {
        return ResponseEntity.ok(analyticsService.getSentimentAnalysis(botId));
    }

    @GetMapping("/bots/{botId}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Métricas de Conversa", description = "Retorna métricas detalhadas de conversas de um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Map<String, Object>> getConversationMetrics(@PathVariable Long botId) {
        return ResponseEntity.ok(analyticsService.getConversationMetrics(botId));
    }
}

