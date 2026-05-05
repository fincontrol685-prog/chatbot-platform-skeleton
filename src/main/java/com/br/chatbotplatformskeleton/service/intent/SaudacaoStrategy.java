package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting SAUDACAO (greeting) intent.
 */
@Component
public class SaudacaoStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "ola", "olá", "bom dia", "boa tarde", "boa noite")) {
            return new IntentAnalysis("SAUDACAO", 0.88d, 0.25d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 75;
    }
}

