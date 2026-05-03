package com.br.chatbotplatformskeleton.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Monitoring & Observability endpoints
 * Exposes health, metrics, and audit endpoints for Prometheus and monitoring tools
 * 
 * Phase 5 - Monitoring & Observability
 */
@Configuration
public class MonitoringConfiguration {

    // Actuator endpoints are auto-configured by Spring Boot
    // Following endpoints are available by default:
    
    // GET /actuator/health - Application health status
    // GET /actuator/metrics - Available metrics list
    // GET /actuator/metrics/{metric.name} - Specific metric details
    // GET /actuator/prometheus - Prometheus format metrics
    // GET /actuator/info - Application info
    // GET /actuator/env - Environment properties
    
    // Configuration via application.properties:
    // management.endpoints.web.exposure.include=health,metrics,prometheus,info
    // management.endpoint.health.show-details=always
    // management.metrics.export.prometheus.enabled=true
}

