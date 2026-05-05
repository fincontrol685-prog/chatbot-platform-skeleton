package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.config.RateLimitingConfiguration;
import com.br.chatbotplatformskeleton.config.RateLimitProperties;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for handling rate limiting logic
 * Provides methods to check and enforce rate limits on requests
 */
@Service
public class RateLimitService {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitService.class);

    private final RateLimitProperties properties;
    private final ConcurrentHashMap<String, Bucket> userBuckets;
    private final ConcurrentHashMap<String, Bucket> ipBuckets;

    public RateLimitService(
        RateLimitProperties properties,
        ConcurrentHashMap<String, Bucket> userBuckets,
        ConcurrentHashMap<String, Bucket> ipBuckets
    ) {
        this.properties = properties;
        this.userBuckets = userBuckets;
        this.ipBuckets = ipBuckets;
    }

    /**
     * Check user-based rate limit
     * Returns true if request is allowed, false if limit exceeded
     */
    public boolean checkUserRateLimit(String userKey) {
        if (!properties.isEnabled()) {
            return true;
        }

        String key = "user:" + userKey;
        Bucket bucket = userBuckets.computeIfAbsent(
            key,
            k -> RateLimitingConfiguration.createUserBucket(properties)
        );

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            logger.debug("Rate limit check passed for user: {} (remaining: {})", userKey, probe.getRemainingTokens());
            return true;
        }

        logger.warn("Rate limit exceeded for user: {}", userKey);
        return false;
    }

    /**
     * Check IP-based rate limit
     * Returns true if request is allowed, false if limit exceeded
     */
    public boolean checkIpRateLimit(String ipAddress) {
        if (!properties.isEnabled()) {
            return true;
        }

        String key = "ip:" + ipAddress;
        Bucket bucket = ipBuckets.computeIfAbsent(
            key,
            k -> RateLimitingConfiguration.createIpBucket(properties)
        );

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            logger.debug("Rate limit check passed for IP: {} (remaining: {})", ipAddress, probe.getRemainingTokens());
            return true;
        }

        logger.warn("Rate limit exceeded for IP: {}", ipAddress);
        return false;
    }

    /**
     * Check analytics endpoint rate limit
     * Uses user-based limiting with different thresholds
     */
    public boolean checkAnalyticsRateLimit(String userKey) {
        if (!properties.isEnabled()) {
            return true;
        }

        String key = "analytics:user:" + userKey;
        Bucket bucket = userBuckets.computeIfAbsent(
            key,
            k -> RateLimitingConfiguration.createAnalyticsBucket(properties)
        );

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            logger.debug("Analytics rate limit check passed for user: {} (remaining: {})", userKey, probe.getRemainingTokens());
            return true;
        }

        logger.warn("Analytics rate limit exceeded for user: {}", userKey);
        return false;
    }

    /**
     * Get remaining tokens for user
     */
    public long getRemainingTokensForUser(String userKey) {
        String key = "user:" + userKey;
        Bucket bucket = userBuckets.get(key);
        if (bucket != null) {
            return bucket.estimateAbilityToConsume(1).getRemainingTokens();
        }
        return properties.getUser().getRequests();
    }

    /**
     * Get remaining tokens for IP
     */
    public long getRemainingTokensForIp(String ipAddress) {
        String key = "ip:" + ipAddress;
        Bucket bucket = ipBuckets.get(key);
        if (bucket != null) {
            return bucket.estimateAbilityToConsume(1).getRemainingTokens();
        }
        return properties.getIp().getRequests();
    }
}
