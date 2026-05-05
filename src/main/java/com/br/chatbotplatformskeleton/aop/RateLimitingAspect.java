package com.br.chatbotplatformskeleton.aop;

import com.br.chatbotplatformskeleton.aop.annotation.RateLimited;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * AOP Aspect for implementing rate limiting using Bucket4j.
 * Intercepts methods annotated with @RateLimited and enforces request limits.
 *
 * Rate limits can be per-user or per-IP based on configuration.
 */
@Aspect
@Component
public class RateLimitingAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingAspect.class);
    private static final int HTTP_TOO_MANY_REQUESTS = 429;

    // Map of buckets for different rate limit keys
    // Key: "bucketName:userId/IP", Value: Bucket instance
    private final Map<String, LocalBucket> buckets = new HashMap<>();

    /**
     * Intercepts methods with @RateLimited annotation and enforces rate limiting.
     *
     * @param joinPoint The join point
     * @param rateLimited The @RateLimited annotation
     * @return Response from method or 429 if rate limit exceeded
     * @throws Throwable If method execution fails
     */
    @Around("@annotation(rateLimited)")
    public Object enforceRateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        String key = extractRateLimitKey(rateLimited);
        LocalBucket bucket = getBucket(key, rateLimited.requestsPerMinute());

        if (bucket.tryConsume(1)) {
            log.debug("Rate limit OK for key: {}", key);
            return joinPoint.proceed();
        } else {
            log.warn("Rate limit exceeded for key: {}", key);
            return buildRateLimitExceededResponse();
        }
    }

    /**
     * Extracts the rate limit key based on user context or IP address.
     *
     * @param rateLimited The annotation
     * @return The rate limit key
     */
    private String extractRateLimitKey(RateLimited rateLimited) {
        if (rateLimited.keyByUser()) {
            return extractUserKey(rateLimited);
        } else {
            return extractIpKey(rateLimited);
        }
    }

    /**
     * Extracts user-based rate limit key.
     *
     * @param rateLimited The annotation
     * @return User ID or "anonymous"
     */
    private String extractUserKey(RateLimited rateLimited) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                return rateLimited.bucketName() + ":user:" + username;
            }
        } catch (Exception e) {
            log.debug("Failed to extract user from security context", e);
        }
        return rateLimited.bucketName() + ":user:anonymous";
    }

    /**
     * Extracts IP-address-based rate limit key.
     *
     * @param rateLimited The annotation
     * @return Client IP address or "unknown"
     */
    private String extractIpKey(RateLimited rateLimited) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String clientIp = getClientIp(request);
                return rateLimited.bucketName() + ":ip:" + clientIp;
            }
        } catch (Exception e) {
            log.debug("Failed to extract IP from request", e);
        }
        return rateLimited.bucketName() + ":ip:unknown";
    }

    /**
     * Gets the client's IP address from the request, handling proxies.
     *
     * @param request The HTTP request
     * @return Client IP address
     */
    private String getClientIp(HttpServletRequest request) {
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
     * Builds a 429 Too Many Requests response.
     *
     * @return ResponseEntity with 429 status
     */
    private ResponseEntity<?> buildRateLimitExceededResponse() {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HTTP_TOO_MANY_REQUESTS);
        errorResponse.put("error", "Too Many Requests");
        errorResponse.put("message", "Rate limit exceeded. Please try again later.");

        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(errorResponse);
    }

    private Bandwidth createLimit(int requestsPerMinute) {
        return Bandwidth.classic(
                requestsPerMinute,
                Refill.intervally(requestsPerMinute, Duration.ofMinutes(1))
        );
    }

    /**
            * Gets or creates a bucket for the given key with specified rate limit.
     *
             * @param key The bucket key
     * @param requestsPerMinute Number of requests allowed per minute
     * @return The bucket instance
     */
    private LocalBucket getBucket(String key, int requestsPerMinute) {
        return buckets.computeIfAbsent(key, k ->
                Bucket4j.builder()
                        .addLimit(createLimit(requestsPerMinute))
                        .build()
        );
    }
}

