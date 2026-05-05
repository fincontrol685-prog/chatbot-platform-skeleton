package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting ENCERRAMENTO (closing) intent.
 */
@Component
public class EncerramentoStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "obrigado", "obrigada", "valeu", "agrade")) {
            return new IntentAnalysis("ENCERRAMENTO", 0.95d, 0.55d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 85; // High priority - closing should be recognized early
    }
}

