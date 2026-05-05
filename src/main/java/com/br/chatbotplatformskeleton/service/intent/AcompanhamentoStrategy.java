package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting ACOMPANHAMENTO (follow-up/tracking) intent.
 */
@Component
public class AcompanhamentoStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "protocolo", "status", "acompanhar", "andamento", "prazo", "retorno")) {
            return new IntentAnalysis("ACOMPANHAMENTO", 0.86d, -0.1d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 60;
    }
}

