package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting ACESSO (access/credentials) intent.
 */
@Component
public class AcessoStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "senha", "login", "acesso", "permiss", "usuario bloqueado", "usuário bloqueado")) {
            return new IntentAnalysis("ACESSO", 0.9d, -0.35d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 65;
    }
}

