package com.br.chatbotplatformskeleton.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Cache configuration properties for Phase 3 optimization.
 * Configure cache TTL and behavior in application.properties or application.yml.
 *
 * Example:
 * cache:
 *   botconfig:
 *     ttl-minutes: 60
 *   intentanalysis:
 *     ttl-minutes: 30
 *   responseplan:
 *     ttl-minutes: 5
 */
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private CacheProfile botConfig = new CacheProfile(60);
    private CacheProfile intentAnalysis = new CacheProfile(30);
    private CacheProfile responsePlan = new CacheProfile(5);

    public static class CacheProfile {
        private long ttlMinutes;
        private long maxSize = 1000;
        private boolean enabled = true;

        public CacheProfile() {}

        public CacheProfile(long ttlMinutes) {
            this.ttlMinutes = ttlMinutes;
        }

        // Getters and Setters
        public long getTtlMinutes() { return ttlMinutes; }
        public void setTtlMinutes(long ttlMinutes) { this.ttlMinutes = ttlMinutes; }

        public long getMaxSize() { return maxSize; }
        public void setMaxSize(long maxSize) { this.maxSize = maxSize; }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    // Getters and Setters
    public CacheProfile getBotConfig() { return botConfig; }
    public void setBotConfig(CacheProfile botConfig) { this.botConfig = botConfig; }

    public CacheProfile getIntentAnalysis() { return intentAnalysis; }
    public void setIntentAnalysis(CacheProfile intentAnalysis) { this.intentAnalysis = intentAnalysis; }

    public CacheProfile getResponsePlan() { return responsePlan; }
    public void setResponsePlan(CacheProfile responsePlan) { this.responsePlan = responsePlan; }
}

