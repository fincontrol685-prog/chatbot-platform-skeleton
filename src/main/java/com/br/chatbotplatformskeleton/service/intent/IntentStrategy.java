package com.br.chatbotplatformskeleton.service.intent;

import java.util.Locale;

/**
 * Strategy interface for detecting and analyzing different user intents.
 * Implements Strategy Pattern to reduce cyclomatic complexity in BotResponseService.
 */
public interface IntentStrategy {

    /**
     * Analyzes the normalized message and determines if it matches this intent.
     *
     * @param normalizedMessage The normalized user message (lowercase)
     * @return IntentAnalysis if matched, null if not applicable
     */
    IntentAnalysis analyze(String normalizedMessage);

    /**
     * Gets the priority of this strategy for execution order.
     * Higher values are executed first.
     *
     * @return Priority (0-100)
     */
    default int getPriority() {
        return 50;
    }

    /**
     * Checks if the message contains any of the given tokens.
     *
     * @param source The text to search in
     * @param tokens The tokens to search for
     * @return true if any token is found
     */
    default boolean containsAny(String source, String... tokens) {
        for (String token : tokens) {
            if (source.contains(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Intent Analysis Record
     */
    record IntentAnalysis(String intent, double confidence, double sentimentScore, boolean sensitive) { }
}

