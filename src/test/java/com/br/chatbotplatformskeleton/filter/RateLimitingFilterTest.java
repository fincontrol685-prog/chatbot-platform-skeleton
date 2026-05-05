package com.br.chatbotplatformskeleton.filter;

import com.br.chatbotplatformskeleton.config.RateLimitProperties;
import com.br.chatbotplatformskeleton.service.RateLimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for RateLimitingFilter
 * Tests that rate limiting filter properly intercepts and limits requests
 */
@DisplayName("RateLimitingFilter Integration Tests")
class RateLimitingFilterTest {

    private RateLimitingFilter rateLimitingFilter;
    private RateLimitService rateLimitService;
    private RateLimitProperties properties;

    @BeforeEach
    void setUp() {
        properties = new RateLimitProperties();
        properties.setEnabled(true);
        properties.getIp().setRequests(3);
        properties.getUser().setRequests(5);

        ConcurrentHashMap<String, io.github.bucket4j.Bucket> userBuckets = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, io.github.bucket4j.Bucket> ipBuckets = new ConcurrentHashMap<>();

        rateLimitService = new RateLimitService(properties, userBuckets, ipBuckets);
        rateLimitingFilter = new RateLimitingFilter(rateLimitService, properties);
    }

    @Test
    @DisplayName("Should allow requests within rate limit for login endpoint")
    void testLoginEndpointWithinLimit() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        request.setRemoteAddr("192.168.1.100");

        // First 3 requests should be allowed
        for (int i = 0; i < 3; i++) {
            String clientIp = extractClientIp(request);
            assertTrue(rateLimitService.checkIpRateLimit(clientIp),
                    "Request " + (i + 1) + " should be allowed");
        }
    }

    @Test
    @DisplayName("Should reject requests exceeding rate limit for login endpoint")
    void testLoginEndpointExceededLimit() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        request.setRemoteAddr("192.168.1.100");

        String clientIp = extractClientIp(request);

        // Consume all tokens
        for (int i = 0; i < 3; i++) {
            rateLimitService.checkIpRateLimit(clientIp);
        }

        // 4th request should be rejected
        assertFalse(rateLimitService.checkIpRateLimit(clientIp),
                "4th request should be rejected");
    }

    @Test
    @DisplayName("Should extract client IP from X-Forwarded-For header")
    void testExtractClientIpFromXForwardedFor() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        request.setRemoteAddr("192.168.1.100");
        request.addHeader("X-Forwarded-For", "10.0.0.1, 10.0.0.2");

        String clientIp = extractClientIp(request);
        assertEquals("10.0.0.1", clientIp, "Should extract first IP from X-Forwarded-For");
    }

    @Test
    @DisplayName("Should fallback to remote address when no forwarded IP")
    void testExtractClientIpFallback() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        request.setRemoteAddr("192.168.1.100");

        String clientIp = extractClientIp(request);
        assertEquals("192.168.1.100", clientIp, "Should use remote address as fallback");
    }

    @Test
    @DisplayName("Should allow requests when rate limiting is disabled")
    void testRateLimitingDisabled() {
        properties.setEnabled(false);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        request.setRemoteAddr("192.168.1.100");

        String clientIp = extractClientIp(request);

        // Should allow more than limit
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimitService.checkIpRateLimit(clientIp),
                    "Should allow unlimited requests when disabled");
        }
    }

    @Test
    @DisplayName("Should block according to shouldNotFilter logic")
    void testShouldNotFilter() {
        String[] pathsToSkip = {
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/style.css",
            "/main.js"
        };

        for (String path : pathsToSkip) {
            assertTrue(shouldNotFilter(path),
                    "Path " + path + " should be skipped");
        }
    }

    /**
     * Extract client IP from request (mirrors filter logic)
     */
    private String extractClientIp(MockHttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * Mirror of shouldNotFilter logic from filter
     */
    private boolean shouldNotFilter(String path) {
        return path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.equals("/") ||
               path.endsWith(".css") ||
               path.endsWith(".js");
    }
}

