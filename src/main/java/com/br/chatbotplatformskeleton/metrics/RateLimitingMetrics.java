package com.br.chatbotplatformskeleton.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

/**
 * Custom metrics for Rate Limiting monitoring
 * Tracks rate limit violations, remaining tokens, and endpoint usage
 * 
 * Phase 5 - Monitoring & Observability
 */
@Component
public class RateLimitingMetrics {

    private final MeterRegistry meterRegistry;
    private final Counter rateLimitViolations;
    private final Counter rateLimitAllowed;
    private final Timer rateLimitCheckDuration;

    public RateLimitingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Counter for rate limit violations
        this.rateLimitViolations = Counter.builder("ratelimit.violations.total")
            .description("Total number of rate limit violations")
            .register(meterRegistry);
        
        // Counter for allowed requests
        this.rateLimitAllowed = Counter.builder("ratelimit.allowed.total")
            .description("Total number of allowed requests")
            .register(meterRegistry);
        
        // Timer for rate limit check duration
        this.rateLimitCheckDuration = Timer.builder("ratelimit.check.duration")
            .description("Time taken to perform rate limit check")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
    }

    /**
     * Record a rate limit violation
     */
    public void recordViolation(String identifierType, String identifier) {
        rateLimitViolations.increment();
        
        // Tag metrics by type and identifier for detailed tracking
        Counter.builder("ratelimit.violations.per_type")
            .tag("type", identifierType)
            .tag("identifier", identifier)
            .register(meterRegistry)
            .increment();
    }

    /**
     * Record an allowed request
     */
    public void recordAllowed(String identifierType, String identifier) {
        rateLimitAllowed.increment();
        
        Counter.builder("ratelimit.allowed.per_type")
            .tag("type", identifierType)
            .tag("identifier", identifier)
            .register(meterRegistry)
            .increment();
    }

    /**
     * Record time taken for rate limit check
     */
    public void recordCheckDuration(long durationNanos) {
        rateLimitCheckDuration.record(durationNanos, java.util.concurrent.TimeUnit.NANOSECONDS);
    }

    /**
     * Record remaining tokens for monitoring
     */
    public void recordRemainingTokens(String identifierType, long remainingTokens) {
        meterRegistry.gauge(
            "ratelimit.remaining.tokens." + identifierType,
            remainingTokens
        );
    }
}

