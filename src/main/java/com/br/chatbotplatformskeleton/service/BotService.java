package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.dto.BotDto;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BotService {

    private final BotRepository botRepository;
    private final ObjectMapper objectMapper;

    public BotService(BotRepository botRepository, ObjectMapper objectMapper) {
        this.botRepository = botRepository;
        this.objectMapper = objectMapper;
    }

    public List<BotDto> listAll() {
        return botRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<BotDto> findById(Long id) {
        return botRepository.findById(id).map(this::toDto);
    }

    public BotDto create(BotDto dto) {
        String normalizedName = normalizeRequired(dto.getName(), "Nome do bot");
        String normalizedKey = normalizeRequired(dto.getKey(), "Chave do bot");

        if (botRepository.existsByKeyIgnoreCase(normalizedKey)) {
            throw new IllegalArgumentException("Ja existe um bot com esta chave");
        }

        Bot bot = new Bot();
        applyChanges(bot, dto, normalizedName, normalizedKey);
        Bot saved = botRepository.save(bot);
        return toDto(saved);
    }

    public Optional<BotDto> update(Long id, BotDto dto) {
        Optional<Bot> existingBot = botRepository.findById(id);
        if (existingBot.isEmpty()) {
            return Optional.empty();
        }

        String normalizedName = normalizeRequired(dto.getName(), "Nome do bot");
        String normalizedKey = normalizeRequired(dto.getKey(), "Chave do bot");

        if (botRepository.existsByKeyIgnoreCaseAndIdNot(normalizedKey, id)) {
            throw new IllegalArgumentException("Ja existe um bot com esta chave");
        }

        Bot bot = existingBot.get();
        applyChanges(bot, dto, normalizedName, normalizedKey);
        Bot saved = botRepository.save(bot);
        return Optional.of(toDto(saved));
    }

    public Optional<BotDto> activate(Long id, boolean active) {
        Optional<Bot> ob = botRepository.findById(id);
        if (ob.isEmpty()) return Optional.empty();
        Bot b = ob.get();
        b.setEnabled(active);
        Bot saved = botRepository.save(b);
        return Optional.of(toDto(saved));
    }

    private void applyChanges(Bot bot, BotDto dto, String normalizedName, String normalizedKey) {
        bot.setName(normalizedName);
        bot.setKey(normalizedKey);
        bot.setEnabled(dto.getEnabled() == null || dto.getEnabled());
        bot.setConfig(normalizeConfig(dto.getConfig()));
    }

    private BotDto toDto(Bot b) {
        BotDto dto = new BotDto();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setKey(b.getKey());
        dto.setEnabled(b.getEnabled());
        dto.setConfig(b.getConfig());
        return dto;
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
