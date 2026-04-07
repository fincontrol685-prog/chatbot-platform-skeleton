package com.br.chatbotplatformskeleton.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Security Headers and Rate Limiting Configuration
 * Implements production-grade security headers and basic rate limiting
 */
@Configuration
public class SecurityHeadersConfig implements WebMvcConfigurer {

    /**
     * Rate Limiting Interceptor
     * Basic implementation: 1000 requests per minute per IP
     */
    public static class RateLimitInterceptor implements HandlerInterceptor {
        private static final int MAX_REQUESTS_PER_MINUTE = 1000;
        private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute
        private final Map<String, RateLimitEntry> requestCounts = new ConcurrentHashMap<>();

        private static class RateLimitEntry {
            AtomicInteger count = new AtomicInteger(0);
            long windowStart = System.currentTimeMillis();

            void reset() {
                count.set(0);
                windowStart = System.currentTimeMillis();
            }

            boolean isExpired() {
                return System.currentTimeMillis() - windowStart > TIME_WINDOW_MS;
            }
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String clientIp = getClientIp(request);
            RateLimitEntry entry = requestCounts.computeIfAbsent(clientIp, k -> new RateLimitEntry());

            if (entry.isExpired()) {
                entry.reset();
            }

            if (entry.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("{\"error\": \"Rate limit exceeded\"}");
                return false;
            }

            response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS_PER_MINUTE));
            response.setHeader("X-RateLimit-Remaining", String.valueOf(MAX_REQUESTS_PER_MINUTE - entry.count.get()));
            response.setHeader("X-RateLimit-Reset", String.valueOf(entry.windowStart + TIME_WINDOW_MS));

            return true;
        }

        private String getClientIp(HttpServletRequest request) {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        }
    }

    /**
     * Security Headers Interceptor
     * Adds production-grade security headers to all responses
     */
    public static class SecurityHeadersInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // Prevent Clickjacking
            response.setHeader("X-Frame-Options", "DENY");

            // Prevent MIME type sniffing
            response.setHeader("X-Content-Type-Options", "nosniff");

            // XSS Protection
            response.setHeader("X-XSS-Protection", "1; mode=block");

            // Referrer Policy
            response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

            // Content Security Policy
            response.setHeader("Content-Security-Policy", 
                "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data: https:; " +
                "font-src 'self'; " +
                "connect-src 'self'; " +
                "frame-ancestors 'none'; " +
                "base-uri 'self'; " +
                "form-action 'self'");

            // HSTS - HTTP Strict-Transport-Security (1 year)
            response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");

            // Permissions Policy (formerly Feature Policy)
            response.setHeader("Permissions-Policy", 
                "accelerometer=(), " +
                "ambient-light-sensor=(), " +
                "autoplay=(), " +
                "battery=(), " +
                "camera=(), " +
                "cross-origin-isolated=(), " +
                "display-capture=(), " +
                "document-domain=(), " +
                "encrypted-media=(), " +
                "execution-while-not-rendered=(), " +
                "execution-while-out-of-viewport=(), " +
                "fullscreen=(), " +
                "geolocation=(), " +
                "gyroscope=(), " +
                "magnetometer=(), " +
                "microphone=(), " +
                "midi=(), " +
                "navigation-override=(), " +
                "payment=(), " +
                "picture-in-picture=(), " +
                "publickey-credentials-get=(), " +
                "speaker-selection=(), " +
                "sync-xhr=(), " +
                "usb=(), " +
                "vr=(), " +
                "wake-lock=(), " +
                "web-share=(), " +
                "xr-spatial-tracking=()");

            // Remove server identification
            response.setHeader("Server", "");

            // Cache Control - Prevent caching sensitive data
            if (request.getRequestURI().contains("/api/")) {
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
            }

            return true;
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityHeadersInterceptor());
        registry.addInterceptor(new RateLimitInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/actuator/**");
    }
}

