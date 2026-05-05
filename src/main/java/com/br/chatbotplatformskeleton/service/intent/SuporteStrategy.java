package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting SUPORTE (support/issue) intent.
 */
@Component
public class SuporteStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "erro", "falha", "nao funciona", "não funciona", "problema", "indispon", "bug")) {
            return new IntentAnalysis("SUPORTE", 0.9d, -0.45d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 70;
    }
}

