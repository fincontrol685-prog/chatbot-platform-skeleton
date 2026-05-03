package com.br.chatbotplatformskeleton.filter;

import com.br.chatbotplatformskeleton.config.RateLimitProperties;
import com.br.chatbotplatformskeleton.service.RateLimitService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for applying rate limiting to requests
 * Checks rate limits based on endpoint and user/IP
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);
    private static final String RATE_LIMIT_HEADER = "X-RateLimit-Remaining";
    private static final String RATE_LIMIT_RESET_HEADER = "X-RateLimit-Reset";

    private final RateLimitService rateLimitService;
    private final RateLimitProperties properties;

    public RateLimitingFilter(RateLimitService rateLimitService, RateLimitProperties properties) {
        this.rateLimitService = rateLimitService;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!properties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestPath = request.getRequestURI();

        // Apply rate limiting to specific endpoints
        if (shouldApplyRateLimit(requestPath)) {
            if (!enforceRateLimit(request, response, requestPath)) {
                return; // Request blocked due to rate limit
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Determine if rate limiting should be applied to this request path
     */
    private boolean shouldApplyRateLimit(String requestPath) {
        return requestPath.startsWith("/api/messages/conversation/") ||
               requestPath.startsWith("/api/auth/") ||
               requestPath.startsWith("/api/analytics/");
    }

    /**
     * Enforce rate limit on request
     * Returns true if allowed, false if rate limit exceeded
     */
    private boolean enforceRateLimit(HttpServletRequest request, HttpServletResponse response, String requestPath)
            throws IOException {

        // Rate limit /api/auth/login endpoints by IP
        if (requestPath.contains("/api/auth/login") ||
            requestPath.contains("/api/auth/register") ||
            requestPath.contains("/api/auth/forgot-password")) {
            return enforceIpRateLimit(request, response);
        }

        // Rate limit /api/analytics/* endpoints by user
        if (requestPath.startsWith("/api/analytics/")) {
            return enforceUserRateLimit(request, response, false, true);
        }

        // Rate limit /api/messages/conversation/{id}/exchange endpoints by user
        if (requestPath.contains("/exchange")) {
            return enforceUserRateLimit(request, response, true, false);
        }

        // Default: allow the request
        return true;
    }

    /**
     * Enforce user-based rate limit (requires authentication)
     */
    private boolean enforceUserRateLimit(HttpServletRequest request, HttpServletResponse response,
                                        boolean isMessageExchange, boolean isAnalytics) throws IOException {
        Long userId = extractUserId();

        if (userId == null) {
            // No authenticated user, allow the request
            return true;
        }

        boolean allowed;
        if (isAnalytics) {
            allowed = rateLimitService.checkAnalyticsRateLimit(userId);
        } else {
            allowed = rateLimitService.checkUserRateLimit(userId);
        }

        if (!allowed) {
            logger.warn("Rate limit exceeded for user: {}", userId);
            response.setStatus(429); // TOO_MANY_REQUESTS
            response.setContentType("application/json");
            response.setHeader(RATE_LIMIT_HEADER, "0");
            response.setHeader(RATE_LIMIT_RESET_HEADER, String.valueOf(System.currentTimeMillis() / 1000 + 60));

            String errorJson = "{\"error\": \"Rate limit exceeded. Maximum requests per minute exceeded.\", " +
                              "\"retryAfter\": 60}";
            response.getWriter().write(errorJson);
            response.getWriter().flush();
            return false;
        }

        response.setHeader(RATE_LIMIT_HEADER, String.valueOf(rateLimitService.getRemainingTokensForUser(userId)));
        return true;
    }

    /**
     * Enforce IP-based rate limit (for unauthenticated endpoints)
     */
    private boolean enforceIpRateLimit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientIp = extractClientIp(request);

        boolean allowed = rateLimitService.checkIpRateLimit(clientIp);

        if (!allowed) {
            logger.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(429); // TOO_MANY_REQUESTS
            response.setContentType("application/json");
            response.setHeader(RATE_LIMIT_HEADER, "0");
            response.setHeader(RATE_LIMIT_RESET_HEADER, String.valueOf(System.currentTimeMillis() / 1000 + 60));

            String errorJson = "{\"error\": \"Rate limit exceeded. Too many login attempts.\", " +
                              "\"retryAfter\": 60}";
            response.getWriter().write(errorJson);
            response.getWriter().flush();
            return false;
        }

        response.setHeader(RATE_LIMIT_HEADER, String.valueOf(rateLimitService.getRemainingTokensForIp(clientIp)));
        return true;
    }

    /**
     * Extract user ID from authentication context
     */
    private Long extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Try to extract userId from custom UserDetails if available
                try {
                    // This assumes the username is the userId as String
                    String username = ((UserDetails) principal).getUsername();
                    return Long.valueOf(username);
                } catch (NumberFormatException e) {
                    logger.debug("Could not extract userId from username: {}", principal);
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Extract client IP from request, handling proxies
     */
    private String extractClientIp(HttpServletRequest request) {
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Don't apply filter to static resources and non-API endpoints
        return path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.equals("/") ||
               path.endsWith(".css") ||
               path.endsWith(".js");
    }
}

