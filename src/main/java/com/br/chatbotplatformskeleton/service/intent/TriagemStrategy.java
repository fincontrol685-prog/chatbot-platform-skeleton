package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Default strategy for unclassified intents (TRIAGEM).
 * Fallback strategy with lowest priority.
 */
@Component
public class TriagemStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        // This is the default fallback - always matches
        double sentiment = determineSentiment(normalizedMessage);
        return new IntentAnalysis("TRIAGEM", 0.58d, sentiment, false);
    }

    @Override
    public int getPriority() {
        return 0; // Lowest priority - last resort
    }

    private double determineSentiment(String normalized) {
        if (containsAny(normalized, "erro", "falha", "ruim", "insatisfeito", "problema", "atraso")) {
            return -0.45d;
        }
        if (containsAny(normalized, "obrigado", "excelente", "otimo", "ótimo", "perfeito")) {
            return 0.45d;
        }
        return 0.05d;
    }
}

