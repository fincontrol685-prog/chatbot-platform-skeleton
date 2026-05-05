package com.br.chatbotplatformskeleton.service.intent;

import org.springframework.stereotype.Component;

/**
 * Strategy for detecting HORARIO (business hours) intent.
 */
@Component
public class HorarioStrategy implements IntentStrategy {

    @Override
    public IntentAnalysis analyze(String normalizedMessage) {
        if (containsAny(normalizedMessage,
            "horario", "horário", "funciona", "atendimento", "abre", "fecha")) {
            return new IntentAnalysis("HORARIO", 0.81d, 0.05d, false);
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 50;
    }
}

