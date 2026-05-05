package com.br.chatbotplatformskeleton.service.response;

import com.br.chatbotplatformskeleton.service.BotResponseService;
import com.br.chatbotplatformskeleton.service.IntentAnalysisRecord;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for composing bot responses based on intent and configuration.
 * Encapsulates response generation logic to reduce complexity in BotResponseService.
 */
@Service
public class ResponseComposer {

    private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\b([A-Z]{2,}-?\\d{3,}|\\d{6,})\\b");

    /**
     * Composes a bot response based on intent analysis and bot configuration.
     *
     * @param config The bot configuration
     * @param analysis The intent analysis
     * @param userMessage The original user message
     * @param firstInteraction Whether this is the first interaction
     * @param protocol The generated protocol number (may be null)
     * @return The composed response text
     */
    public String composeResponse(
        BotResponseService.BotConfig config,
        IntentAnalysisRecord analysis,
        String userMessage,
        boolean firstInteraction,
        String protocol
    ) {
        List<String> sections = new ArrayList<>();
        String intent = analysis.intent();

        if (firstInteraction && shouldPresentOperatingContext(intent)) {
            sections.add(buildOperatingContext(config));
        }

        switch (intent) {
            case "SAUDACAO" -> composeSaudacao(sections, config);
            case "SUPORTE" -> composeSuporte(sections, config, protocol);
            case "ACESSO" -> composeAcesso(sections, config, protocol);
            case "ACOMPANHAMENTO" -> composeAcompanhamento(sections, config, userMessage);
            case "COMERCIAL" -> composeComercial(sections, config);
            case "HORARIO" -> composeHorario(sections, config);
            case "ESCALACAO" -> composeEscalacao(sections, config);
            case "ENCERRAMENTO" -> composeEncerramento(sections, config);
            default -> composeTriagem(sections, config, firstInteraction);
        }

        // Add operational notes if detailed style
        if (!config.notes.isBlank() && "detalhado".equalsIgnoreCase(config.profile.responseStyle)) {
            sections.add("Observacao operacional: " + config.notes);
        }

        // Add signature unless closing
        String signature = config.messages.signature;
        if (!signature.isBlank() && !"ENCERRAMENTO".equals(intent)) {
            sections.add(signature);
        }

        return String.join("\n\n", sections);
    }

    private void composeSaudacao(List<String> sections, BotResponseService.BotConfig config) {
        sections.add(config.messages.welcome);
        if (!"conciso".equalsIgnoreCase(config.profile.responseStyle)) {
            sections.add("Atuo em " + config.knowledge.scope +
                " e posso seguir com triagem, direcionamento e organizacao do proximo passo.");
        }
    }

    private void composeSuporte(List<String> sections, BotResponseService.BotConfig config, String protocol) {
        sections.add(openingLine(config, "Entendi o problema e vou organizar a triagem com objetividade."));
        if (protocol != null) {
            sections.add("Referencia inicial do atendimento: " + protocol + ".");
        }
        sections.add(buildChecklistSection(
            "Para eu responder com mais assertividade, preciso de:",
            config.knowledge.requiredContext,
            List.of(
                "o que aconteceu e em qual fluxo ou sistema",
                "quando o problema comecou e se ele e recorrente",
                "qual impacto isso gerou na operacao ou no usuario"
            )
        ));
        appendScopeAndRestrictions(sections, config, true);
    }

    private void composeAcesso(List<String> sections, BotResponseService.BotConfig config, String protocol) {
        sections.add(openingLine(config, "Posso estruturar a liberacao ou correção de acesso com mais precisao."));
        if (protocol != null) {
            sections.add("Referencia inicial do atendimento: " + protocol + ".");
        }
        sections.add(buildChecklistSection(
            "Me informe:",
            config.knowledge.requiredContext,
            List.of(
                "qual usuario, email ou perfil esta envolvido",
                "qual sistema ou ambiente precisa de acesso",
                "se o bloqueio e total, parcial ou por permissao especifica"
            )
        ));
        appendScopeAndRestrictions(sections, config, true);
    }

    private void composeAcompanhamento(List<String> sections, BotResponseService.BotConfig config, String userMessage) {
        String reference = extractReference(userMessage);
        if (reference != null) {
            sections.add("Recebi a referencia " + reference + ".");
        } else {
            sections.add("Posso organizar o acompanhamento, mas preciso de uma referencia objetiva.");
        }
        sections.add("Nesta base eu nao consulto status em tempo real, mas consigo deixar a analise pronta para o time com:\n" +
            bullet("protocolo, pedido ou identificador relacionado") +
            bullet("nome do cliente ou area solicitante") +
            bullet("qual retorno voce espera neste momento"));
    }

    private void composeComercial(List<String> sections, BotResponseService.BotConfig config) {
        sections.add(openingLine(config, "Consigo qualificar sua necessidade para direcionar a proposta certa."));
        sections.add(buildChecklistSection(
            "Para seguir sem retrabalho, me envie:",
            config.knowledge.requiredContext,
            List.of(
                "objetivo principal da conversa ou da contratacao",
                "porte da operacao, equipe ou volume esperado",
                "prazo desejado para avaliacao ou implantacao"
            )
        ));
        if (Boolean.TRUE.equals(config.guidelines.collectLead)) {
            sections.add("Se fizer sentido, inclua nome, empresa e melhor canal de retorno para acelerar a continuidade.");
        }
    }

    private void composeHorario(List<String> sections, BotResponseService.BotConfig config) {
        sections.add("Horario principal configurado para este atendimento: " + config.guidelines.businessHours + ".");
        sections.add("Se sua demanda ja puder ser registrada agora, eu organizo o contexto e o time continua a partir daqui.");
    }

    private void composeEscalacao(List<String> sections, BotResponseService.BotConfig config) {
        sections.add(config.messages.escalation);
        sections.add(buildChecklistSection(
            "Antes da transferencia, deixe este resumo pronto:",
            config.knowledge.handoffContext,
            List.of(
                "fato principal do caso",
                "impacto atual",
                "nivel de urgencia",
                "proximo passo esperado"
            )
        ));
        appendScopeAndRestrictions(sections, config, false);
    }

    private void composeEncerramento(List<String> sections, BotResponseService.BotConfig config) {
        sections.add(config.messages.closing);
    }

    private void composeTriagem(List<String> sections, BotResponseService.BotConfig config, boolean firstInteraction) {
        if (firstInteraction) {
            sections.add(config.messages.welcome);
        }
        sections.add(config.messages.fallback);
        sections.add(buildChecklistSection(
            "Se puder, detalhe estes pontos para eu te conduzir de forma objetiva:",
            config.knowledge.requiredContext,
            List.of(
                "assunto principal",
                "contexto atual",
                "resultado esperado"
            )
        ));
    }

    private void appendScopeAndRestrictions(List<String> sections, BotResponseService.BotConfig config, boolean includeSuccessCriteria) {
        sections.add("Escopo considerado: " + config.knowledge.scope + ".");
        if (includeSuccessCriteria && !"conciso".equalsIgnoreCase(config.profile.responseStyle)) {
            sections.add("Objetivo desta triagem: " + config.knowledge.successCriteria + ".");
        }
        sections.add("Restricoes operacionais: " + config.knowledge.restrictions + ".");
    }

    private String openingLine(BotResponseService.BotConfig config, String defaultLine) {
        return switch (config.profile.tone.toLowerCase(Locale.ROOT)) {
            case "consultivo" -> defaultLine + " Vou estruturar os dados necessarios para o proximo passo.";
            case "acolhedor" -> "Entendi seu contexto. " + defaultLine;
            case "objetivo" -> defaultLine;
            default -> defaultLine + " Vou seguir com uma triagem clara.";
        };
    }

    private boolean shouldPresentOperatingContext(String intent) {
        return !"ENCERRAMENTO".equals(intent);
    }

    private String buildOperatingContext(BotResponseService.BotConfig config) {
        return "Contexto deste atendimento: " + config.profile.assistantRole +
            " do " + config.profile.department +
            ", com foco em " + config.profile.targetAudience +
            " pelo canal " + config.profile.primaryChannel + ".";
    }

    private String buildChecklistSection(String title, String configuredContext, List<String> fallbackItems) {
        List<String> items = parseChecklistItems(configuredContext);
        if (items.isEmpty()) {
            items = fallbackItems;
        }

        StringBuilder builder = new StringBuilder(title).append("\n");
        for (String item : items) {
            builder.append(bullet(item));
        }
        return builder.toString().trim();
    }

    private List<String> parseChecklistItems(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return List.of();
        }

        return Pattern.compile("\\r?\\n|;")
            .splitAsStream(rawValue)
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .toList();
    }

    private String extractReference(String content) {
        Matcher matcher = REFERENCE_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String bullet(String value) {
        return "- " + value + "\n";
    }
}
