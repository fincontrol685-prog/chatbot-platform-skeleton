package com.br.chatbotplatformskeleton.service.cache;

import com.br.chatbotplatformskeleton.service.intent.IntentAnalyzer;
import com.br.chatbotplatformskeleton.service.intent.IntentStrategy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Cached wrapper around IntentAnalyzer for improved performance.
 * Caches intent analysis results for normalized messages.
 * This reduces repeated string processing for common user inputs.
 */
@Service
public class CachedIntentAnalyzer {

    private final IntentAnalyzer intendAnalyzer;

    public CachedIntentAnalyzer(IntentAnalyzer intendAnalyzer) {
        this.intendAnalyzer = intendAnalyzer;
    }

    /**
     * Analyzes message intent with caching.
     * Results are cached for 30 minutes based on message hash.
     * Common queries like "olá", "erro", "obrigado" will be cached.
     *
     * @param normalizedMessage The normalized message to analyze
     * @return Cached or computed intent analysis
     */
    @Cacheable(value = "intentAnalysis", key = "#normalizedMessage.hashCode()",
               cacheManager = "cacheManager", unless = "#result == null")
    public IntentStrategy.IntentAnalysis analyzeAndCache(String normalizedMessage) {
        return intendAnalyzer.analyze(normalizedMessage);
    }
}

