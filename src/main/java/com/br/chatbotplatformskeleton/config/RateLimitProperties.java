package com.br.chatbotplatformskeleton.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Rate Limiting configuration properties
 * Load from application.properties under ratelimit prefix
 */
@ConfigurationProperties(prefix = "ratelimit")
public class RateLimitProperties {

    private UserLimitConfig user;
    private IpLimitConfig ip;
    private AnalyticsLimitConfig analytics;
    private boolean enabled = true;

    public RateLimitProperties() {
        this.user = new UserLimitConfig();
        this.ip = new IpLimitConfig();
        this.analytics = new AnalyticsLimitConfig();
    }

    /**
     * Configuration for user-based rate limiting
     * Used for authenticated endpoints like /api/messages/conversation/{id}/exchange
     */
    public static class UserLimitConfig {
        private int requests = 10;           // 10 requests
        private int intervalMinutes = 1;     // per minute

        public int getRequests() { return requests; }
        public void setRequests(int requests) { this.requests = requests; }

        public int getIntervalMinutes() { return intervalMinutes; }
        public void setIntervalMinutes(int intervalMinutes) { this.intervalMinutes = intervalMinutes; }
    }

    /**
     * Configuration for IP-based rate limiting
     * Used for unauthenticated endpoints like /api/auth/login
     */
    public static class IpLimitConfig {
        private int requests = 5;            // 5 requests
        private int intervalMinutes = 1;     // per minute

        public int getRequests() { return requests; }
        public void setRequests(int requests) { this.requests = requests; }

        public int getIntervalMinutes() { return intervalMinutes; }
        public void setIntervalMinutes(int intervalMinutes) { this.intervalMinutes = intervalMinutes; }
    }

    /**
     * Configuration for analytics endpoints rate limiting
     * Used for /api/analytics/* endpoints
     */
    public static class AnalyticsLimitConfig {
        private int requests = 30;           // 30 requests
        private int intervalMinutes = 1;     // per minute

        public int getRequests() { return requests; }
        public void setRequests(int requests) { this.requests = requests; }

        public int getIntervalMinutes() { return intervalMinutes; }
        public void setIntervalMinutes(int intervalMinutes) { this.intervalMinutes = intervalMinutes; }
    }

    // Getters and Setters
    public UserLimitConfig getUser() { return user; }
    public void setUser(UserLimitConfig user) { this.user = user; }

    public IpLimitConfig getIp() { return ip; }
    public void setIp(IpLimitConfig ip) { this.ip = ip; }

    public AnalyticsLimitConfig getAnalytics() { return analytics; }
    public void setAnalytics(AnalyticsLimitConfig analytics) { this.analytics = analytics; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

