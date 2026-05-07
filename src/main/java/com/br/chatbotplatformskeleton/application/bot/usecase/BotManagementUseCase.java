package com.br.chatbotplatformskeleton.application.bot.usecase;

import java.util.List;
import java.util.Optional;

public interface BotManagementUseCase {

    List<BotView> listAll();

    Optional<BotView> findById(Long id);

    BotView create(UpsertBotCommand command);

    Optional<BotView> update(Long id, UpsertBotCommand command);

    Optional<BotView> activate(Long id, boolean active);
}
