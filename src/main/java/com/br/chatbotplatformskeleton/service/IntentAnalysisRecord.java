package com.br.chatbotplatformskeleton.service;

/**
 * Record for Intent Analysis results from the IntentAnalyzer.
 * Extracted from BotResponseService to support strategy pattern.
 */
public record IntentAnalysisRecord(
    String intent,
    double confidence,
    double sentimentScore,
    boolean sensitive
) { }

