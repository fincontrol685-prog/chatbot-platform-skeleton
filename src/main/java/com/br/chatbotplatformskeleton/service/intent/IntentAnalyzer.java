package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

/**
 * Service that implements Chain of Responsibility pattern for intent detection.
 * Uses multiple IntentStrategy implementations in priority order.
 */
@Service
public class IntentAnalyzer {

    private final List<IntentStrategy> strategies;

    public IntentAnalyzer(List<IntentStrategy> strategies) {
        // Sort strategies by priority (descending)
        this.strategies = strategies.stream()
            .sorted(Comparator.comparingInt(IntentStrategy::getPriority).reversed())
            .toList();
    }

    /**
     * Analyzes a message using the Chain of Responsibility pattern.
     * Tries each strategy in priority order until one matches.
     *
     * @param normalizedMessage The normalized user message (lowercase)
     * @return The IntentAnalysis result from the first matching strategy
     */
    public IntentStrategy.IntentAnalysis analyze(String normalizedMessage) {
        for (IntentStrategy strategy : strategies) {
            IntentStrategy.IntentAnalysis result = strategy.analyze(normalizedMessage);
            if (result != null) {
                return result;
            }
        }

        // Should never reach here as TriagemStrategy is always a fallback
        return new IntentStrategy.IntentAnalysis("TRIAGEM", 0.5d, 0.0d, false);
    }
}

