package com.br.chatbotplatformskeleton.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Custom metrics for Caching monitoring
 * Tracks cache hits, misses, evictions, and performance
 * 
 * Phase 5 - Monitoring & Observability
 */
@Component
public class CachingMetrics {

    private final MeterRegistry meterRegistry;
    private final Counter cacheHits;
    private final Counter cacheMisses;
    private final Counter cacheEvictions;
    private final Timer cacheAccessTime;

    public CachingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Counter for cache hits
        this.cacheHits = Counter.builder("cache.hits.total")
            .description("Total number of cache hits")
            .register(meterRegistry);
        
        // Counter for cache misses
        this.cacheMisses = Counter.builder("cache.misses.total")
            .description("Total number of cache misses")
            .register(meterRegistry);
        
        // Counter for cache evictions
        this.cacheEvictions = Counter.builder("cache.evictions.total")
            .description("Total number of cache evictions due to TTL")
            .register(meterRegistry);
        
        // Timer for cache access time
        this.cacheAccessTime = Timer.builder("cache.access.time")
            .description("Time taken to access cache")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
    }

    /**
     * Record a cache hit for a specific cache
     */
    public void recordHit(String cacheName) {
        cacheHits.increment();
        
        Counter.builder("cache.hits.per_cache")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
    }

    /**
     * Record a cache miss for a specific cache
     */
    public void recordMiss(String cacheName) {
        cacheMisses.increment();
        
        Counter.builder("cache.misses.per_cache")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
    }

    /**
     * Record a cache eviction
     */
    public void recordEviction(String cacheName) {
        cacheEvictions.increment();
        
        Counter.builder("cache.evictions.per_cache")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
    }

    /**
     * Record cache access time
     */
    public void recordAccessTime(String cacheName, long durationNanos) {
        cacheAccessTime.record(durationNanos, TimeUnit.NANOSECONDS);
        
        Timer.builder("cache.access.time.per_cache")
            .tag("cache", cacheName)
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry)
            .record(durationNanos, TimeUnit.NANOSECONDS);
    }

    /**
     * Calculate and record cache hit rate
     */
    public void recordCacheHitRate(String cacheName) {
        double hits = cacheHits.count();
        double misses = cacheMisses.count();
        double total = hits + misses;
        
        if (total > 0) {
            double hitRate = (hits / total) * 100;
            meterRegistry.gauge("cache.hit.rate." + cacheName, hitRate);
        }
    }

    /**
     * Record current cache size
     */
    public void recordCacheSize(String cacheName, long size) {
        meterRegistry.gauge("cache.size." + cacheName, size);
    }
}

