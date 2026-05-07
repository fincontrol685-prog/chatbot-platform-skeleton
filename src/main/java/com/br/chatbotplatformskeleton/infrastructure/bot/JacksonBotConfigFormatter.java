package com.br.chatbotplatformskeleton.infrastructure.bot;

import com.br.chatbotplatformskeleton.application.port.out.BotConfigFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JacksonBotConfigFormatter implements BotConfigFormatter {

    private static final Logger log = LoggerFactory.getLogger(JacksonBotConfigFormatter.class);

    private final ObjectMapper objectMapper;

    public JacksonBotConfigFormatter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String normalize(String rawConfig) {
        if (rawConfig == null || rawConfig.trim().isEmpty()) {
            return null;
        }

        String trimmedConfig = rawConfig.trim();
        if (!looksLikeJson(trimmedConfig)) {
            return trimmedConfig;
        }

        try {
            String decodedConfig = StringEscapeUtils.unescapeHtml4(trimmedConfig);
            JsonNode jsonNode = objectMapper.readTree(decodedConfig);
            String formatted = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            log.debug("Bot config formatted successfully, size: {} chars", formatted.length());
            return formatted;
        } catch (JsonProcessingException ex) {
            log.error("Failed to parse bot config JSON. Raw config (first 500 chars): {}",
                trimmedConfig.length() > 500 ? trimmedConfig.substring(0, 500) + "..." : trimmedConfig, ex);
            throw new IllegalArgumentException("Configuracao do bot deve estar em JSON valido: " + ex.getMessage());
        }
    }

    private boolean looksLikeJson(String value) {
        return value.startsWith("{") || value.startsWith("[");
    }
}
