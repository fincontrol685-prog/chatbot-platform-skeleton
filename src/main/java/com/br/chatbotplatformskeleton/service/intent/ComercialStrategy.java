package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting COMERCIAL (sales/pricing) intent.
 */
@Component
public class ComercialStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "plano", "preco", "preço", "orcamento", "orçamento", "proposta", "contratar", "demo")) {
            return new IntentAnalysis("COMERCIAL", 0.89d, 0.18d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 55;
    }
}

