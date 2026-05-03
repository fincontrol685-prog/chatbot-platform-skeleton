package com.br.chatbotplatformskeleton.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration for Rate Limiting with Bucket4j.
 * Provides bucket creation and management for different rate limit strategies.
 * 
 * Phase 4 - Performance & Security Enhancement
 */
@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitingConfiguration {

    /**
     * Creates a bucket for per-user rate limiting
     * Default: 10 requests per minute per user
     */
    public static Bucket createUserBucket(RateLimitProperties properties) {
        Bandwidth limit = Bandwidth.classic(
            properties.getUser().getRequests(),
            Refill.intervally(
                properties.getUser().getRequests(),
                Duration.ofMinutes(properties.getUser().getIntervalMinutes())
            )
        );
        return Bucket4j.builder().addLimit(limit).build();
    }

    /**
     * Creates a bucket for per-IP rate limiting
     * Default: 5 requests per minute per IP (for login)
     */
    public static Bucket createIpBucket(RateLimitProperties properties) {
        Bandwidth limit = Bandwidth.classic(
            properties.getIp().getRequests(),
            Refill.intervally(
                properties.getIp().getRequests(),
                Duration.ofMinutes(properties.getIp().getIntervalMinutes())
            )
        );
        return Bucket4j.builder().addLimit(limit).build();
    }

    /**
     * Creates a bucket for analytics endpoints
     * Default: 30 requests per minute per user
     */
    public static Bucket createAnalyticsBucket(RateLimitProperties properties) {
        Bandwidth limit = Bandwidth.classic(
            properties.getAnalytics().getRequests(),
            Refill.intervally(
                properties.getAnalytics().getRequests(),
                Duration.ofMinutes(properties.getAnalytics().getIntervalMinutes())
            )
        );
        return Bucket4j.builder().addLimit(limit).build();
    }

    /**
     * In-memory store for user-based buckets
     * In production, use distributed cache (Redis)
     */
    @Bean
    public ConcurrentHashMap<String, Bucket> userBuckets() {
        return new ConcurrentHashMap<>();
    }

    /**
     * In-memory store for IP-based buckets
     * In production, use distributed cache (Redis)
     */
    @Bean
    public ConcurrentHashMap<String, Bucket> ipBuckets() {
        return new ConcurrentHashMap<>();
    }
}

