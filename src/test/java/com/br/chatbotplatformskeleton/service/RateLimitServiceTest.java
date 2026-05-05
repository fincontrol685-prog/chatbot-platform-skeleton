package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.config.RateLimitProperties;
import com.br.chatbotplatformskeleton.config.RateLimitingConfiguration;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimitService
 * Tests rate limiting logic for users, IPs, and analytics endpoints
 */
@DisplayName("RateLimitService Tests")
class RateLimitServiceTest {

    private RateLimitService rateLimitService;
    private RateLimitProperties properties;
    private ConcurrentHashMap<String, Bucket> userBuckets;
    private ConcurrentHashMap<String, Bucket> ipBuckets;

    @BeforeEach
    void setUp() {
        properties = new RateLimitProperties();
        properties.setEnabled(true);

        // Configure test limits
        properties.getUser().setRequests(5);
        properties.getUser().setIntervalMinutes(1);

        properties.getIp().setRequests(3);
        properties.getIp().setIntervalMinutes(1);

        properties.getAnalytics().setRequests(10);
        properties.getAnalytics().setIntervalMinutes(1);

        userBuckets = new ConcurrentHashMap<>();
        ipBuckets = new ConcurrentHashMap<>();

        rateLimitService = new RateLimitService(properties, userBuckets, ipBuckets);
    }

    @Test
    @DisplayName("Should allow requests within user rate limit")
    void testUserRateLimitAllowed() {
        String userId = "user-1";

        // First 5 requests should be allowed
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitService.checkUserRateLimit(userId),
                    "Request " + (i + 1) + " should be allowed");
        }
    }

    @Test
    @DisplayName("Should reject requests exceeding user rate limit")
    void testUserRateLimitExceeded() {
        String userId = "user-1";

        // Consume all 5 tokens
        for (int i = 0; i < 5; i++) {
            rateLimitService.checkUserRateLimit(userId);
        }

        // 6th request should be rejected
        assertFalse(rateLimitService.checkUserRateLimit(userId),
                "6th request should be rejected");
    }

    @Test
    @DisplayName("Should allow requests within IP rate limit")
    void testIpRateLimitAllowed() {
        String ipAddress = "192.168.1.100";

        // First 3 requests should be allowed
        for (int i = 0; i < 3; i++) {
            assertTrue(rateLimitService.checkIpRateLimit(ipAddress),
                    "Request " + (i + 1) + " should be allowed");
        }
    }

    @Test
    @DisplayName("Should reject requests exceeding IP rate limit")
    void testIpRateLimitExceeded() {
        String ipAddress = "192.168.1.100";

        // Consume all 3 tokens
        for (int i = 0; i < 3; i++) {
            rateLimitService.checkIpRateLimit(ipAddress);
        }

        // 4th request should be rejected
        assertFalse(rateLimitService.checkIpRateLimit(ipAddress),
                "4th request should be rejected");
    }

    @Test
    @DisplayName("Should isolate rate limits between different users")
    void testUserIsolation() {
        String user1 = "user-1";
        String user2 = "user-2";

        // Consume all tokens for user 1
        for (int i = 0; i < 5; i++) {
            rateLimitService.checkUserRateLimit(user1);
        }

        // User 1 should be rate limited
        assertFalse(rateLimitService.checkUserRateLimit(user1));

        // User 2 should still have tokens
        assertTrue(rateLimitService.checkUserRateLimit(user2));
    }

    @Test
    @DisplayName("Should isolate rate limits between different IPs")
    void testIpIsolation() {
        String ip1 = "192.168.1.100";
        String ip2 = "192.168.1.101";

        // Consume all tokens for IP 1
        for (int i = 0; i < 3; i++) {
            rateLimitService.checkIpRateLimit(ip1);
        }

        // IP 1 should be rate limited
        assertFalse(rateLimitService.checkIpRateLimit(ip1));

        // IP 2 should still have tokens
        assertTrue(rateLimitService.checkIpRateLimit(ip2));
    }

    @Test
    @DisplayName("Should support analytics rate limiting")
    void testAnalyticsRateLimit() {
        String userId = "user-1";

        // First 10 requests should be allowed
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimitService.checkAnalyticsRateLimit(userId),
                    "Request " + (i + 1) + " should be allowed");
        }

        // 11th request should be rejected
        assertFalse(rateLimitService.checkAnalyticsRateLimit(userId),
                "11th request should be rejected");
    }

    @Test
    @DisplayName("Should return correct remaining tokens")
    void testRemainingTokens() {
        String userId = "user-1";

        // Initially should have 5 tokens
        assertEquals(5, rateLimitService.getRemainingTokensForUser(userId));

        // Consume 2 tokens
        rateLimitService.checkUserRateLimit(userId);
        rateLimitService.checkUserRateLimit(userId);

        // Should have 3 tokens left
        assertEquals(3, rateLimitService.getRemainingTokensForUser(userId));
    }

    @Test
    @DisplayName("Should disable rate limiting when disabled")
    void testRateLimitingDisabled() {
        properties.setEnabled(false);
        String userId = "user-1";

        // Should allow unlimited requests
        for (int i = 0; i < 100; i++) {
            assertTrue(rateLimitService.checkUserRateLimit(userId));
        }
    }
}
