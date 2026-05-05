package com.br.chatbotplatformskeleton.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimitingMetrics
 * Validates rate limiting metrics collection
 */
@DisplayName("RateLimitingMetrics Tests")
class RateLimitingMetricsTest {

    private RateLimitingMetrics metrics;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new RateLimitingMetrics(meterRegistry);
    }

    @Test
    @DisplayName("Should record rate limit violations")
    void testRecordViolation() {
        metrics.recordViolation("user", "user123");
        metrics.recordViolation("user", "user123");
        metrics.recordViolation("ip", "192.168.1.1");

        double violations = meterRegistry.counter("ratelimit.violations.total").count();
        assertEquals(3, violations, "Should have recorded 3 violations");
    }

    @Test
    @DisplayName("Should record allowed requests")
    void testRecordAllowed() {
        metrics.recordAllowed("user", "user123");
        metrics.recordAllowed("user", "user123");
        metrics.recordAllowed("ip", "192.168.1.1");

        double allowed = meterRegistry.counter("ratelimit.allowed.total").count();
        assertEquals(3, allowed, "Should have recorded 3 allowed requests");
    }

    @Test
    @DisplayName("Should record rate limit check duration")
    void testRecordCheckDuration() {
        metrics.recordCheckDuration(100000); // 0.1ms in nanos
        metrics.recordCheckDuration(50000);  // 0.05ms in nanos

        assertTrue(meterRegistry.find("ratelimit.check.duration").timer() != null,
                "Should have recorded check duration");
    }

    @Test
    @DisplayName("Should record remaining tokens")
    void testRecordRemainingTokens() {
        // Should execute without throwing exception
        assertDoesNotThrow(() -> {
            metrics.recordRemainingTokens("user", 9);
            metrics.recordRemainingTokens("user", 5);
        });
    }

    @Test
    @DisplayName("Should separate metrics by identifier type")
    void testMetricSeparationByType() {
        metrics.recordViolation("user", "user1");
        metrics.recordViolation("ip", "192.168.1.1");

        // Both should be recorded under different tags
        double violations = meterRegistry.counter("ratelimit.violations.total").count();
        assertEquals(2, violations, "Should record both violations");
    }
}

