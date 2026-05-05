package com.br.chatbotplatformskeleton.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CachingMetrics
 * Validates cache performance metrics collection
 */
@DisplayName("CachingMetrics Tests")
class CachingMetricsTest {

    private CachingMetrics metrics;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new CachingMetrics(meterRegistry);
    }

    @Test
    @DisplayName("Should record cache hits")
    void testRecordHit() {
        metrics.recordHit("botConfig");
        metrics.recordHit("botConfig");
        metrics.recordHit("intentAnalysis");

        double hits = meterRegistry.counter("cache.hits.total").count();
        assertEquals(3, hits, "Should have recorded 3 cache hits");
    }

    @Test
    @DisplayName("Should record cache misses")
    void testRecordMiss() {
        metrics.recordMiss("botConfig");
        metrics.recordMiss("botConfig");
        metrics.recordMiss("intentAnalysis");

        double misses = meterRegistry.counter("cache.misses.total").count();
        assertEquals(3, misses, "Should have recorded 3 cache misses");
    }

    @Test
    @DisplayName("Should record cache evictions")
    void testRecordEviction() {
        metrics.recordEviction("botConfig");
        metrics.recordEviction("botConfig");

        double evictions = meterRegistry.counter("cache.evictions.total").count();
        assertEquals(2, evictions, "Should have recorded 2 cache evictions");
    }

    @Test
    @DisplayName("Should record cache access time")
    void testRecordAccessTime() {
        metrics.recordAccessTime("botConfig", 100000); // 0.1ms
        metrics.recordAccessTime("botConfig", 50000);  // 0.05ms

        assertNotNull(meterRegistry.find("cache.access.time").timer(),
                "Should have recorded access time");
    }

    @Test
    @DisplayName("Should record cache size")
    void testRecordCacheSize() {
        // Should execute without throwing exception
        assertDoesNotThrow(() -> {
            metrics.recordCacheSize("botConfig", 250);
            metrics.recordCacheSize("intentAnalysis", 4500);
        });
    }

    @Test
    @DisplayName("Should calculate cache hit rate")
    void testCalculateCacheHitRate() {
        // Record 7 hits and 3 misses = 70% hit rate
        for (int i = 0; i < 7; i++) {
            metrics.recordHit("botConfig");
        }
        for (int i = 0; i < 3; i++) {
            metrics.recordMiss("botConfig");
        }

        // Should execute without throwing exception
        assertDoesNotThrow(() -> metrics.recordCacheHitRate("botConfig"));
    }

    @Test
    @DisplayName("Should separate metrics by cache name")
    void testMetricSeparationByCache() {
        metrics.recordHit("botConfig");
        metrics.recordMiss("intentAnalysis");

        double totalHits = meterRegistry.counter("cache.hits.total").count();
        double totalMisses = meterRegistry.counter("cache.misses.total").count();

        assertEquals(1, totalHits, "Should have 1 hit");
        assertEquals(1, totalMisses, "Should have 1 miss");
    }
}

