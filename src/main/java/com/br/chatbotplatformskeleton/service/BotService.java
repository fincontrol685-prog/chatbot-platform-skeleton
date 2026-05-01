package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.dto.BotDto;
import com.br.chatbotplatformskeleton.mapper.BotMapper;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BotService {

    private static final Logger log = LoggerFactory.getLogger(BotService.class);
    private final BotRepository botRepository;
    private final ObjectMapper objectMapper;
    private final BotMapper botMapper;

    public BotService(BotRepository botRepository, ObjectMapper objectMapper, BotMapper botMapper) {
        this.botRepository = botRepository;
        this.objectMapper = objectMapper;
        this.botMapper = botMapper;
    }

    public List<BotDto> listAll() {
        log.info("Listing all bots");
        return botRepository.findAll().stream().map(botMapper::toDto).collect(Collectors.toList());
    }

    public Optional<BotDto> findById(Long id) {
        log.debug("Finding bot with id: {}", id);
        return botRepository.findById(id).map(botMapper::toDto);
    }

    public BotDto create(BotDto dto) {
        log.info("Creating new bot with name: {}", dto.getName());
        String normalizedName = normalizeRequired(dto.getName(), "Nome do bot");
        String normalizedKey = normalizeRequired(dto.getKey(), "Chave do bot");

        if (botRepository.existsByKeyIgnoreCase(normalizedKey)) {
            log.warn("Bot creation failed: key already exists - {}", normalizedKey);
            throw new IllegalArgumentException("Ja existe um bot com esta chave");
        }

        Bot bot = new Bot();
        applyChanges(bot, dto, normalizedName, normalizedKey);
        Bot saved = botRepository.save(bot);
        log.info("Bot created successfully with id: {}", saved.getId());
        return botMapper.toDto(saved);
    }

    public Optional<BotDto> update(Long id, BotDto dto) {
        log.info("Updating bot with id: {}", id);
        Optional<Bot> existingBot = botRepository.findById(id);
        if (existingBot.isEmpty()) {
            log.warn("Bot update failed: bot not found - id: {}", id);
            return Optional.empty();
        }

        String normalizedName = normalizeRequired(dto.getName(), "Nome do bot");
        String normalizedKey = normalizeRequired(dto.getKey(), "Chave do bot");

        if (botRepository.existsByKeyIgnoreCaseAndIdNot(normalizedKey, id)) {
            log.warn("Bot update failed: key already exists - {}", normalizedKey);
            throw new IllegalArgumentException("Ja existe um bot com esta chave");
        }

        Bot bot = existingBot.get();
        applyChanges(bot, dto, normalizedName, normalizedKey);
        Bot saved = botRepository.save(bot);
        log.info("Bot updated successfully with id: {}", saved.getId());
        return Optional.of(botMapper.toDto(saved));
    }

    public Optional<BotDto> activate(Long id, boolean active) {
        log.info("Setting bot {} status to: {}", id, active ? "ACTIVE" : "INACTIVE");
        Optional<Bot> ob = botRepository.findById(id);
        if (ob.isEmpty()) {
            log.warn("Bot activation failed: bot not found - id: {}", id);
            return Optional.empty();
        }
        Bot b = ob.get();
        b.setEnabled(active);
        Bot saved = botRepository.save(b);
        return Optional.of(botMapper.toDto(saved));
    }

    private void applyChanges(Bot bot, BotDto dto, String normalizedName, String normalizedKey) {
        bot.setName(normalizedName);
        bot.setKey(normalizedKey);
        bot.setEnabled(dto.getEnabled() == null || dto.getEnabled());
        bot.setConfig(normalizeConfig(dto.getConfig()));
    }


    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " e obrigatorio");
        }
        return value.trim();
    }

    private String normalizeConfig(String rawConfig) {
        if (rawConfig == null || rawConfig.trim().isEmpty()) {
            return null;
        }

        String trimmedConfig = rawConfig.trim();
        if (!looksLikeJson(trimmedConfig)) {
            return trimmedConfig;
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(trimmedConfig);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Configuracao do bot deve estar em JSON valido");
        }
    }

    private boolean looksLikeJson(String value) {
        return value.startsWith("{") || value.startsWith("[");
    }
}
