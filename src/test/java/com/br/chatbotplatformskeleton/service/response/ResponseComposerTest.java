package com.br.chatbotplatformskeleton.service.response;

import com.br.chatbotplatformskeleton.service.BotResponseService;
import com.br.chatbotplatformskeleton.service.IntentAnalysisRecord;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseComposerTest {

    private final ResponseComposer responseComposer = new ResponseComposer();

    @Test
    void composeResponseShouldUseConfiguredOperationalContextAndChecklist() {
        BotResponseService.BotConfig config = new BotResponseService.BotConfig();
        config.profile.assistantRole = "Analista virtual de suporte";
        config.profile.department = "Suporte enterprise";
        config.profile.targetAudience = "clientes B2B";
        config.profile.primaryChannel = "whatsapp";
        config.profile.tone = "profissional";
        config.profile.responseStyle = "equilibrado";
        config.knowledge.scope = "incidentes e acessos";
        config.knowledge.restrictions = "nao compartilhar dados sensiveis";
        config.knowledge.successCriteria = "direcionar sem retrabalho";
        config.knowledge.requiredContext = "sistema afetado; impacto atual; urgencia";
        config.messages.signature = "Central enterprise";

        String response = responseComposer.composeResponse(
            config,
            new IntentAnalysisRecord("SUPORTE", 0.91d, 0.15d, false),
            "Estou com erro no portal",
            true,
            "BOT-1-123"
        );

        assertThat(response).contains("Contexto deste atendimento: Analista virtual de suporte do Suporte enterprise");
        assertThat(response).contains("com foco em clientes B2B pelo canal whatsapp");
        assertThat(response).contains("- sistema afetado");
        assertThat(response).contains("- impacto atual");
        assertThat(response).contains("- urgencia");
        assertThat(response).contains("Central enterprise");
    }

    @Test
    void composeResponseShouldUseConfiguredHandoffChecklistForEscalation() {
        BotResponseService.BotConfig config = new BotResponseService.BotConfig();
        config.profile.assistantRole = "Assistente operacional";
        config.profile.department = "Service desk";
        config.profile.targetAudience = "colaboradores";
        config.profile.primaryChannel = "interno";
        config.profile.tone = "objetivo";
        config.profile.responseStyle = "conciso";
        config.knowledge.scope = "suporte interno";
        config.knowledge.restrictions = "seguir fluxo oficial";
        config.knowledge.handoffContext = "resumo do caso\nimpacto atual\narea responsavel esperada";
        config.messages.escalation = "Vou encaminhar para o especialista.";
        config.messages.signature = "";

        String response = responseComposer.composeResponse(
            config,
            new IntentAnalysisRecord("ESCALACAO", 0.88d, -0.10d, true),
            "Preciso falar com humano",
            false,
            null
        );

        assertThat(response).contains("Antes da transferencia, deixe este resumo pronto:");
        assertThat(response).contains("- resumo do caso");
        assertThat(response).contains("- impacto atual");
        assertThat(response).contains("- area responsavel esperada");
    }
}
