package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting ESCALACAO (escalation) intent.
 * Highest priority for sensitive/urgent cases.
 */
@Component
public class EscalacaoStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "reclam", "processo", "advog", "cancel", "urgente",
            "vazamento", "segur", "fraude", "critico")) {
            return new IntentAnalysis("ESCALACAO", 0.93d, -0.75d, true);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 100; // Highest priority - check sensitive cases first
    }
}

