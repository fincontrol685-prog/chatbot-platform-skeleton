package com.br.chatbotplatformskeleton.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.time.Instant;

/**
 * Structured JSON logging utility for Phase 5 monitoring
 * Provides consistent, structured logging for all components
 */
@Component
public class StructuredLogger {

    private final ObjectMapper objectMapper;

    public StructuredLogger() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Log a structured event
     */
    public void logEvent(Logger logger, String eventType, Map<String, Object> context) {
        Map<String, Object> logEntry = new LinkedHashMap<>();
        logEntry.put("timestamp", Instant.now().toString());
        logEntry.put("event_type", eventType);
        logEntry.putAll(context);

        try {
            logger.info(objectMapper.writeValueAsString(logEntry));
        } catch (Exception e) {
            logger.error("Failed to log structured event", e);
        }
    }

    /**
     * Log a rate limit event
     */
    public void logRateLimitEvent(Logger logger, String action, String identifier,
                                   String identifierType, long remainingTokens) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("action", action);
        context.put("identifier", identifier);
        context.put("identifier_type", identifierType);
        context.put("remaining_tokens", remainingTokens);

        logEvent(logger, "RATE_LIMIT", context);
    }

    /**
     * Log a cache event
     */
    public void logCacheEvent(Logger logger, String action, String cacheName,
                               String keyHash, long durationMs) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("action", action);
        context.put("cache_name", cacheName);
        context.put("key_hash", keyHash);
        context.put("duration_ms", durationMs);

        logEvent(logger, "CACHE", context);
    }

    /**
     * Log a performance event
     */
    public void logPerformanceEvent(Logger logger, String operationName,
                                    long durationMs, String status) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("operation", operationName);
        context.put("duration_ms", durationMs);
        context.put("status", status);

        logEvent(logger, "PERFORMANCE", context);
    }

    /**
     * Log a security event
     */
    public void logSecurityEvent(Logger logger, String eventName, String severity,
                                 String details) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("event_name", eventName);
        context.put("severity", severity);
        context.put("details", details);

        logEvent(logger, "SECURITY", context);
    }
}

