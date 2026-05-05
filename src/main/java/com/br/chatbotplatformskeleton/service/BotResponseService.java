package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.ConversationMessage;
import com.br.chatbotplatformskeleton.service.intent.IntentAnalyzer;
import com.br.chatbotplatformskeleton.service.intent.IntentStrategy;
import com.br.chatbotplatformskeleton.service.response.ResponseComposer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotResponseService {

    private static final String SYSTEM_USERNAME = "bot.system";

    private final ObjectMapper objectMapper;
    private final IntentAnalyzer intentAnalyzer;
    private final ResponseComposer responseComposer;

    public BotResponseService(ObjectMapper objectMapper, IntentAnalyzer intentAnalyzer, ResponseComposer responseComposer) {
        this.objectMapper = objectMapper;
        this.intentAnalyzer = intentAnalyzer;
        this.responseComposer = responseComposer;
    }

    public String getSystemUsername() {
        return SYSTEM_USERNAME;
    }

    public ResponsePlan buildResponsePlan(
        Conversation conversation,
        List<ConversationMessage> conversationHistory,
        String userMessageContent
    ) {
        long startedAt = System.nanoTime();

        String normalizedMessage = normalizeContent(userMessageContent);
        if (normalizedMessage.isBlank()) {
            throw new IllegalArgumentException("Mensagem e obrigatoria");
        }

        BotConfig config = readConfig(conversation.getBot().getConfig());
        IntentStrategy.IntentAnalysis analysis = intentAnalyzer.analyze(normalizedMessage);
        boolean firstInteraction = conversationHistory.stream()
            .filter(message -> "USER".equalsIgnoreCase(message.getMessageType()))
            .count() <= 1;
        String protocol = shouldProvideProtocol(config, analysis) ? generateProtocol(conversation) : null;
        IntentAnalysisRecord analysisRecord = new IntentAnalysisRecord(
            analysis.intent(), analysis.confidence(), analysis.sentimentScore(), analysis.sensitive()
        );
        String botContent = responseComposer.composeResponse(config, analysisRecord, normalizedMessage, firstInteraction, protocol);
        long responseTimeMs = Math.max(1L, Duration.ofNanos(System.nanoTime() - startedAt).toMillis());

        return new ResponsePlan(
            analysis.intent(),
            analysis.confidence(),
            analysis.sentimentScore(),
            botContent,
            responseTimeMs,
            buildMetadata(analysis, protocol, config),
            analysis.sensitive() || analysis.confidence() < 0.5d
        );
    }

    /**
     * Reads and parses bot configuration from JSON string.
     * Results are cached per bot (1 hour TTL) to reduce JSON parsing overhead.
     *
     * @param rawConfig The raw JSON configuration string
     * @return Parsed and enriched BotConfig with defaults
     */
    @Cacheable(value = "botConfig", key = "#rawConfig.hashCode()", unless = "#result == null")
    public BotConfig readConfig(String rawConfig) {
        if (rawConfig == null || rawConfig.isBlank()) {
            return defaultConfig();
        }

        try {
            // Defensive: decode HTML entities in case config contains escaped content
            String decodedConfig = StringEscapeUtils.unescapeHtml4(rawConfig);
            BotConfig parsedConfig = objectMapper.readValue(decodedConfig, BotConfig.class);
            return enrichDefaults(parsedConfig);
        } catch (JsonProcessingException ignored) {
            return defaultConfig();
        }
    }

    private BotConfig defaultConfig() {
        return enrichDefaults(new BotConfig());
    }

    private BotConfig enrichDefaults(BotConfig config) {
        BotConfig safeConfig = config == null ? new BotConfig() : config;
        if (safeConfig.profile == null) {
            safeConfig.profile = new BotProfile();
        }
        if (safeConfig.messages == null) {
            safeConfig.messages = new BotMessages();
        }
        if (safeConfig.guidelines == null) {
            safeConfig.guidelines = new BotGuidelines();
        }
        if (safeConfig.knowledge == null) {
            safeConfig.knowledge = new BotKnowledge();
        }

        safeConfig.profile.assistantRole = defaultValue(safeConfig.profile.assistantRole, "assistente virtual");
        safeConfig.profile.department = defaultValue(safeConfig.profile.department, "operacao");
        safeConfig.profile.targetAudience = defaultValue(safeConfig.profile.targetAudience, "clientes da operacao");
        safeConfig.profile.language = defaultValue(safeConfig.profile.language, "pt-BR");
        safeConfig.profile.primaryChannel = defaultValue(safeConfig.profile.primaryChannel, "site");
        safeConfig.profile.tone = defaultValue(safeConfig.profile.tone, "profissional");
        safeConfig.profile.responseStyle = defaultValue(safeConfig.profile.responseStyle, "equilibrado");

        safeConfig.messages.welcome = defaultValue(
            safeConfig.messages.welcome,
            "Ola! Sou a assistente virtual da operacao e posso organizar sua solicitacao com clareza."
        );
        safeConfig.messages.fallback = defaultValue(
            safeConfig.messages.fallback,
            "Nao identifiquei o contexto suficiente. Me informe objetivo, impacto e urgencia para eu seguir."
        );
        safeConfig.messages.escalation = defaultValue(
            safeConfig.messages.escalation,
            "Vou encaminhar o caso para um especialista humano com o contexto ja consolidado."
        );
        safeConfig.messages.closing = defaultValue(
            safeConfig.messages.closing,
            "Atendimento concluido. Se surgir algo novo, posso seguir a partir deste contexto."
        );
        safeConfig.messages.offline = defaultValue(
            safeConfig.messages.offline,
            "Estamos fora do horario principal neste momento, mas sua solicitacao pode ser registrada por aqui."
        );
        safeConfig.messages.signature = defaultValue(safeConfig.messages.signature, "Central de atendimento");

        safeConfig.guidelines.businessHours = defaultValue(safeConfig.guidelines.businessHours, "Seg a Sex, 08:00 as 18:00");
        if (safeConfig.guidelines.firstResponseSlaMinutes == null) {
            safeConfig.guidelines.firstResponseSlaMinutes = 5;
        }
        if (safeConfig.guidelines.collectLead == null) {
            safeConfig.guidelines.collectLead = true;
        }
        if (safeConfig.guidelines.requestProtocol == null) {
            safeConfig.guidelines.requestProtocol = true;
        }
        if (safeConfig.guidelines.escalateSensitiveCases == null) {
            safeConfig.guidelines.escalateSensitiveCases = true;
        }
        if (safeConfig.guidelines.allowEmoji == null) {
            safeConfig.guidelines.allowEmoji = false;
        }

        safeConfig.knowledge.scope = defaultValue(
            safeConfig.knowledge.scope,
            "Triagem, duvidas frequentes e direcionamento para o time correto."
        );
        safeConfig.knowledge.restrictions = defaultValue(
            safeConfig.knowledge.restrictions,
            "Nao prometer excecoes fora de politica nem compartilhar dados sensiveis."
        );
        safeConfig.knowledge.successCriteria = defaultValue(
            safeConfig.knowledge.successCriteria,
            "Responder com clareza e definir o proximo passo sem retrabalho."
        );
        safeConfig.knowledge.requiredContext = defaultValue(
            safeConfig.knowledge.requiredContext,
            "objetivo; contexto atual; impacto; urgencia"
        );
        safeConfig.knowledge.handoffContext = defaultValue(
            safeConfig.knowledge.handoffContext,
            "resumo do caso; impacto atual; proximo passo esperado"
        );

        safeConfig.notes = defaultValue(safeConfig.notes, "");
        return safeConfig;
    }


    private boolean shouldProvideProtocol(BotConfig config, IntentStrategy.IntentAnalysis analysis) {
        return Boolean.TRUE.equals(config.guidelines.requestProtocol)
            && ("SUPORTE".equals(analysis.intent()) || "ACESSO".equals(analysis.intent()) || analysis.sensitive());
    }

    private String buildMetadata(IntentStrategy.IntentAnalysis analysis, String protocol, BotConfig config) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("intent", analysis.intent());
        metadata.put("confidence", analysis.confidence());
        metadata.put("sentimentScore", analysis.sentimentScore());
        metadata.put("sensitive", analysis.sensitive());
        metadata.put("protocol", protocol);
        metadata.put("assistantRole", config.profile.assistantRole);
        metadata.put("department", config.profile.department);
        metadata.put("targetAudience", config.profile.targetAudience);
        metadata.put("language", config.profile.language);
        metadata.put("tone", config.profile.tone);
        metadata.put("responseStyle", config.profile.responseStyle);
        metadata.put("channel", config.profile.primaryChannel);

        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException ignored) {
            return "{\"intent\":\"" + analysis.intent() + "\"}";
        }
    }

    private String generateProtocol(Conversation conversation) {
        return "BOT-" + conversation.getId() + "-" + System.currentTimeMillis();
    }

    private String normalizeContent(String content) {
        if (content == null) {
            return "";
        }
        return content.trim().replaceAll("\\s+", " ");
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }


    public record ResponsePlan(
        String intent,
        double confidence,
        double sentimentScore,
        String botContent,
        long responseTimeMs,
        String metadata,
        boolean flaggedForReview
    ) { }


    public static class BotConfig {
        public BotProfile profile = new BotProfile();
        public BotMessages messages = new BotMessages();
        public BotGuidelines guidelines = new BotGuidelines();
        public BotKnowledge knowledge = new BotKnowledge();
        public String notes = "";
    }

    public static class BotProfile {
        public String assistantRole;
        public String department;
        public String targetAudience;
        public String language;
        public String primaryChannel;
        public String tone;
        public String responseStyle;
    }

    public static class BotMessages {
        public String welcome;
        public String fallback;
        public String escalation;
        public String closing;
        public String offline;
        public String signature;
    }

    public static class BotGuidelines {
        public String businessHours;
        public Integer firstResponseSlaMinutes;
        public Boolean collectLead;
        public Boolean requestProtocol;
        public Boolean escalateSensitiveCases;
        public Boolean allowEmoji;
    }

    public static class BotKnowledge {
        public String scope;
        public String restrictions;
        public String successCriteria;
        public String requiredContext;
        public String handoffContext;
    }
}
