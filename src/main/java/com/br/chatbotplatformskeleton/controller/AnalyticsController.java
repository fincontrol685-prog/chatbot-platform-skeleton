package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }


    @GetMapping("/bots/{botId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Map<String, Object>> getBotAnalytics(@PathVariable Long botId) {
        return ResponseEntity.ok(analyticsService.getBotAnalytics(botId));
    }

    @GetMapping("/bots/{botId}/sentiment")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Map<String, Object>> getSentimentAnalysis(@PathVariable Long botId) {
        return ResponseEntity.ok(analyticsService.getSentimentAnalysis(botId));
    }

    @GetMapping("/bots/{botId}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Map<String, Object>> getConversationMetrics(@PathVariable Long botId) {
        return ResponseEntity.ok(analyticsService.getConversationMetrics(botId));
    }
}

