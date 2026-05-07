package com.br.chatbotplatformskeleton.application.port.out;

import com.br.chatbotplatformskeleton.application.bot.model.BotModel;

import java.util.List;
import java.util.Optional;

public interface BotPersistencePort {

    List<BotModel> findAll();

    Optional<BotModel> findById(Long id);

    boolean existsByKeyIgnoreCase(String key);

    boolean existsByKeyIgnoreCaseAndIdNot(String key, Long id);

    BotModel save(BotModel bot);
}
