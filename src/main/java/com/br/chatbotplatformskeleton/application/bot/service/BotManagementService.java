package com.br.chatbotplatformskeleton.application.bot.service;

import com.br.chatbotplatformskeleton.application.bot.model.BotModel;
import com.br.chatbotplatformskeleton.application.bot.usecase.BotManagementUseCase;
import com.br.chatbotplatformskeleton.application.bot.usecase.BotView;
import com.br.chatbotplatformskeleton.application.bot.usecase.UpsertBotCommand;
import com.br.chatbotplatformskeleton.application.port.out.BotConfigFormatter;
import com.br.chatbotplatformskeleton.application.port.out.BotPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BotManagementService implements BotManagementUseCase {

    private static final Logger log = LoggerFactory.getLogger(BotManagementService.class);

    private final BotPersistencePort botPersistencePort;
    private final BotConfigFormatter botConfigFormatter;

    public BotManagementService(BotPersistencePort botPersistencePort, BotConfigFormatter botConfigFormatter) {
        this.botPersistencePort = Objects.requireNonNull(botPersistencePort, "botPersistencePort must not be null");
        this.botConfigFormatter = Objects.requireNonNull(botConfigFormatter, "botConfigFormatter must not be null");
    }

    @Override
    public List<BotView> listAll() {
        log.info("Listing all bots");
        return botPersistencePort.findAll().stream()
            .map(this::toView)
            .toList();
    }

    @Override
    public Optional<BotView> findById(Long id) {
        log.debug("Finding bot with id: {}", id);
        return botPersistencePort.findById(id).map(this::toView);
    }

    @Override
    public BotView create(UpsertBotCommand command) {
        log.info("Creating new bot with name: {}", command.name());
        String normalizedName = normalizeRequired(command.name(), "Nome do bot");
        String normalizedKey = normalizeRequired(command.key(), "Chave do bot");

        if (botPersistencePort.existsByKeyIgnoreCase(normalizedKey)) {
            log.warn("Bot creation failed: key already exists - {}", normalizedKey);
            throw new IllegalArgumentException("Ja existe um bot com esta chave");
        }

        BotModel bot = new BotModel();
        applyChanges(bot, command, normalizedName, normalizedKey);
        BotModel saved = botPersistencePort.save(bot);
        log.info("Bot created successfully with id: {}", saved.getId());
        return toView(saved);
    }

    @Override
    public Optional<BotView> update(Long id, UpsertBotCommand command) {
        log.info("Updating bot with id: {}", id);
        Optional<BotModel> existingBot = botPersistencePort.findById(id);
        if (existingBot.isEmpty()) {
            log.warn("Bot update failed: bot not found - id: {}", id);
            return Optional.empty();
        }

        String normalizedName = normalizeRequired(command.name(), "Nome do bot");
        String normalizedKey = normalizeRequired(command.key(), "Chave do bot");

        if (botPersistencePort.existsByKeyIgnoreCaseAndIdNot(normalizedKey, id)) {
            log.warn("Bot update failed: key already exists - {}", normalizedKey);
            throw new IllegalArgumentException("Ja existe um bot com esta chave");
        }

        BotModel bot = existingBot.get();
        applyChanges(bot, command, normalizedName, normalizedKey);
        BotModel saved = botPersistencePort.save(bot);
        log.info("Bot updated successfully with id: {}", saved.getId());
        return Optional.of(toView(saved));
    }

    @Override
    public Optional<BotView> activate(Long id, boolean active) {
        log.info("Setting bot {} status to: {}", id, active ? "ACTIVE" : "INACTIVE");
        Optional<BotModel> bot = botPersistencePort.findById(id);
        if (bot.isEmpty()) {
            log.warn("Bot activation failed: bot not found - id: {}", id);
            return Optional.empty();
        }

        BotModel existing = bot.get();
        existing.setEnabled(active);
        return Optional.of(toView(botPersistencePort.save(existing)));
    }

    private void applyChanges(BotModel bot, UpsertBotCommand command, String normalizedName, String normalizedKey) {
        bot.setName(normalizedName);
        bot.setKey(normalizedKey);
        bot.setEnabled(command.enabled() == null || command.enabled());
        bot.setConfig(botConfigFormatter.normalize(command.config()));
    }

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " e obrigatorio");
        }
        return value.trim();
    }

    private BotView toView(BotModel bot) {
        return new BotView(bot.getId(), bot.getName(), bot.getKey(), bot.getEnabled(), bot.getConfig());
    }
}
