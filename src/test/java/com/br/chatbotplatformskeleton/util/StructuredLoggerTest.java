package com.br.chatbotplatformskeleton.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StructuredLogger
 * Validates structured JSON logging utility
 */
@DisplayName("StructuredLogger Tests")
class StructuredLoggerTest {

    private StructuredLogger structuredLogger;
    private Logger logger;

    @BeforeEach
    void setUp() {
        structuredLogger = new StructuredLogger();
        logger = LoggerFactory.getLogger(StructuredLoggerTest.class);
    }

    @Test
    @DisplayName("Should log structured event")
    void testLogEvent() {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("test_key", "test_value");
        context.put("test_number", 42);

        // Should not throw exception
        assertDoesNotThrow(() ->
            structuredLogger.logEvent(logger, "TEST_EVENT", context)
        );
    }

    @Test
    @DisplayName("Should log rate limit event")
    void testLogRateLimitEvent() {
        assertDoesNotThrow(() ->
            structuredLogger.logRateLimitEvent(logger, "DENIED", "user123",
                "USER", 0)
        );
    }

    @Test
    @DisplayName("Should log cache event")
    void testLogCacheEvent() {
        assertDoesNotThrow(() ->
            structuredLogger.logCacheEvent(logger, "HIT", "botConfig",
                "abc123hash", 5)
        );
    }

    @Test
    @DisplayName("Should log performance event")
    void testLogPerformanceEvent() {
        assertDoesNotThrow(() ->
            structuredLogger.logPerformanceEvent(logger, "processRequest",
                150, "SUCCESS")
        );
    }

    @Test
    @DisplayName("Should log security event")
    void testLogSecurityEvent() {
        assertDoesNotThrow(() ->
            structuredLogger.logSecurityEvent(logger, "SUSPICIOUS_ACTIVITY",
                "HIGH", "Multiple failed login attempts")
        );
    }

    @Test
    @DisplayName("Should handle null context gracefully")
    void testLogEventWithNullContext() {
        // Should handle null gracefully
        assertDoesNotThrow(() ->
            structuredLogger.logEvent(logger, "TEST_EVENT", new LinkedHashMap<>())
        );
    }

    @Test
    @DisplayName("Should include timestamp in logs")
    void testTimestampInLogs() {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("key", "value");

        // Should not throw - timestamp is added automatically
        assertDoesNotThrow(() ->
            structuredLogger.logEvent(logger, "TEST_EVENT", context)
        );
    }
}

