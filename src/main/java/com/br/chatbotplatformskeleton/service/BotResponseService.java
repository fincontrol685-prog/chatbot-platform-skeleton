package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.ConversationMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotResponseService {

    private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\b([A-Z]{2,}-?\\d{3,}|\\d{6,})\\b");
    private static final String SYSTEM_USERNAME = "bot.system";

    private final ObjectMapper objectMapper;

    public BotResponseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
        IntentAnalysis analysis = analyzeMessage(normalizedMessage);
        boolean firstInteraction = conversationHistory.stream()
            .filter(message -> "USER".equalsIgnoreCase(message.getMessageType()))
            .count() <= 1;
        String protocol = shouldProvideProtocol(config, analysis) ? generateProtocol(conversation) : null;
        String botContent = composeResponse(config, analysis, normalizedMessage, firstInteraction, protocol);
        long responseTimeMs = Math.max(1L, Duration.ofNanos(System.nanoTime() - startedAt).toMillis());

        return new ResponsePlan(
            analysis.intent,
            analysis.confidence,
            analysis.sentimentScore,
            botContent,
            responseTimeMs,
            buildMetadata(analysis, protocol, config),
            analysis.sensitive || analysis.confidence < 0.5d
        );
    }

    private BotConfig readConfig(String rawConfig) {
        if (rawConfig == null || rawConfig.isBlank()) {
            return defaultConfig();
        }

        try {
            BotConfig parsedConfig = objectMapper.readValue(rawConfig, BotConfig.class);
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

        safeConfig.notes = defaultValue(safeConfig.notes, "");
        return safeConfig;
    }

    private IntentAnalysis analyzeMessage(String message) {
        String normalized = message.toLowerCase(Locale.ROOT);
        boolean sensitive = containsAny(normalized,
            "reclam", "processo", "advog", "cancel", "urgente", "vazamento", "segur", "fraude", "critico");

        if (containsAny(normalized, "obrigado", "obrigada", "valeu", "agrade")) {
            return new IntentAnalysis("ENCERRAMENTO", 0.95d, 0.55d, false);
        }
        if (sensitive) {
            return new IntentAnalysis("ESCALACAO", 0.93d, -0.75d, true);
        }
        if (containsAny(normalized, "ola", "olá", "bom dia", "boa tarde", "boa noite")) {
            return new IntentAnalysis("SAUDACAO", 0.88d, 0.25d, false);
        }
        if (containsAny(normalized, "senha", "login", "acesso", "permiss", "usuario bloqueado", "usuário bloqueado")) {
            return new IntentAnalysis("ACESSO", 0.9d, -0.35d, false);
        }
        if (containsAny(normalized, "erro", "falha", "nao funciona", "não funciona", "problema", "indispon", "bug")) {
            return new IntentAnalysis("SUPORTE", 0.9d, -0.45d, false);
        }
        if (containsAny(normalized, "protocolo", "status", "acompanhar", "andamento", "prazo", "retorno")) {
            return new IntentAnalysis("ACOMPANHAMENTO", 0.86d, -0.1d, false);
        }
        if (containsAny(normalized, "plano", "preco", "preço", "orcamento", "orçamento", "proposta", "contratar", "demo")) {
            return new IntentAnalysis("COMERCIAL", 0.89d, 0.18d, false);
        }
        if (containsAny(normalized, "horario", "horário", "funciona", "atendimento", "abre", "fecha")) {
            return new IntentAnalysis("HORARIO", 0.81d, 0.05d, false);
        }
        return new IntentAnalysis("TRIAGEM", 0.58d, determineSentiment(normalized), false);
    }

    private double determineSentiment(String normalized) {
        if (containsAny(normalized, "erro", "falha", "ruim", "insatisfeito", "problema", "atraso")) {
            return -0.45d;
        }
        if (containsAny(normalized, "obrigado", "excelente", "otimo", "ótimo", "perfeito")) {
            return 0.45d;
        }
        return 0.05d;
    }

    private boolean shouldProvideProtocol(BotConfig config, IntentAnalysis analysis) {
        return Boolean.TRUE.equals(config.guidelines.requestProtocol)
            && ("SUPORTE".equals(analysis.intent) || "ACESSO".equals(analysis.intent) || analysis.sensitive);
    }

    private String composeResponse(
        BotConfig config,
        IntentAnalysis analysis,
        String userMessage,
        boolean firstInteraction,
        String protocol
    ) {
        List<String> sections = new ArrayList<>();

        switch (analysis.intent) {
            case "SAUDACAO" -> {
                sections.add(config.messages.welcome);
                if (!"conciso".equalsIgnoreCase(config.profile.responseStyle)) {
                    sections.add("Atuo em " + config.knowledge.scope + " e posso seguir com triagem, direcionamento e organizacao do proximo passo.");
                }
            }
            case "SUPORTE" -> {
                sections.add(openingLine(config, "Entendi o problema e vou organizar a triagem com objetividade."));
                if (protocol != null) {
                    sections.add("Referencia inicial do atendimento: " + protocol + ".");
                }
                sections.add("Para eu responder com mais assertividade, preciso de:\n"
                    + bullet("o que aconteceu e em qual fluxo ou sistema")
                    + bullet("quando o problema comecou e se ele e recorrente")
                    + bullet("qual impacto isso gerou na operacao ou no usuario"));
                appendScopeAndRestrictions(sections, config, true);
            }
            case "ACESSO" -> {
                sections.add(openingLine(config, "Posso estruturar a liberacao ou correção de acesso com mais precisao."));
                if (protocol != null) {
                    sections.add("Referencia inicial do atendimento: " + protocol + ".");
                }
                sections.add("Me informe:\n"
                    + bullet("qual usuario, email ou perfil esta envolvido")
                    + bullet("qual sistema ou ambiente precisa de acesso")
                    + bullet("se o bloqueio e total, parcial ou por permissao especifica"));
                appendScopeAndRestrictions(sections, config, true);
            }
            case "ACOMPANHAMENTO" -> {
                String reference = extractReference(userMessage);
                if (reference != null) {
                    sections.add("Recebi a referencia " + reference + ".");
                } else {
                    sections.add("Posso organizar o acompanhamento, mas preciso de uma referencia objetiva.");
                }
                sections.add("Nesta base eu nao consulto status em tempo real, mas consigo deixar a analise pronta para o time com:\n"
                    + bullet("protocolo, pedido ou identificador relacionado")
                    + bullet("nome do cliente ou area solicitante")
                    + bullet("qual retorno voce espera neste momento"));
            }
            case "COMERCIAL" -> {
                sections.add(openingLine(config, "Consigo qualificar sua necessidade para direcionar a proposta certa."));
                sections.add("Para seguir sem retrabalho, me envie:\n"
                    + bullet("objetivo principal da conversa ou da contratacao")
                    + bullet("porte da operacao, equipe ou volume esperado")
                    + bullet("prazo desejado para avaliacao ou implantacao"));
                if (Boolean.TRUE.equals(config.guidelines.collectLead)) {
                    sections.add("Se fizer sentido, inclua nome, empresa e melhor canal de retorno para acelerar a continuidade.");
                }
            }
            case "HORARIO" -> {
                sections.add("Horario principal configurado para este atendimento: " + config.guidelines.businessHours + ".");
                sections.add("Se sua demanda ja puder ser registrada agora, eu organizo o contexto e o time continua a partir daqui.");
            }
            case "ESCALACAO" -> {
                sections.add(config.messages.escalation);
                sections.add("Antes da transferencia, descreva em uma frase o fato principal, o impacto atual e o nivel de urgencia para evitar perda de contexto.");
                appendScopeAndRestrictions(sections, config, false);
            }
            case "ENCERRAMENTO" -> {
                sections.add(config.messages.closing);
            }
            default -> {
                if (firstInteraction) {
                    sections.add(config.messages.welcome);
                }
                sections.add(config.messages.fallback);
                sections.add("Se puder, detalhe assunto, contexto atual e resultado esperado para eu te conduzir de forma objetiva.");
            }
        }

        if (!config.notes.isBlank() && "detalhado".equalsIgnoreCase(config.profile.responseStyle)) {
            sections.add("Observacao operacional: " + config.notes);
        }

        String signature = config.messages.signature;
        if (!signature.isBlank() && !"ENCERRAMENTO".equals(analysis.intent)) {
            sections.add(signature);
        }

        return String.join("\n\n", sections);
    }

    private void appendScopeAndRestrictions(List<String> sections, BotConfig config, boolean includeSuccessCriteria) {
        sections.add("Escopo considerado: " + config.knowledge.scope + ".");
        if (includeSuccessCriteria && !"conciso".equalsIgnoreCase(config.profile.responseStyle)) {
            sections.add("Objetivo desta triagem: " + config.knowledge.successCriteria + ".");
        }
        sections.add("Restricoes operacionais: " + config.knowledge.restrictions + ".");
    }

    private String openingLine(BotConfig config, String defaultLine) {
        return switch (config.profile.tone.toLowerCase(Locale.ROOT)) {
            case "consultivo" -> defaultLine + " Vou estruturar os dados necessarios para o proximo passo.";
            case "acolhedor" -> "Entendi seu contexto. " + defaultLine;
            case "objetivo" -> defaultLine;
            default -> defaultLine + " Vou seguir com uma triagem clara.";
        };
    }

    private String buildMetadata(IntentAnalysis analysis, String protocol, BotConfig config) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("intent", analysis.intent);
        metadata.put("confidence", analysis.confidence);
        metadata.put("sentimentScore", analysis.sentimentScore);
        metadata.put("sensitive", analysis.sensitive);
        metadata.put("protocol", protocol);
        metadata.put("tone", config.profile.tone);
        metadata.put("responseStyle", config.profile.responseStyle);
        metadata.put("channel", config.profile.primaryChannel);

        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException ignored) {
            return "{\"intent\":\"" + analysis.intent + "\"}";
        }
    }

    private String generateProtocol(Conversation conversation) {
        return "BOT-" + conversation.getId() + "-" + System.currentTimeMillis();
    }

    private String extractReference(String content) {
        Matcher matcher = REFERENCE_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }

    private boolean containsAny(String source, String... tokens) {
        for (String token : tokens) {
            if (source.contains(token)) {
                return true;
            }
        }
        return false;
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

    private String bullet(String value) {
        return "- " + value + "\n";
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

    private record IntentAnalysis(String intent, double confidence, double sentimentScore, boolean sensitive) { }

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
    }
}
