package com.br.chatbotplatformskeleton.infrastructure.persistence;

import com.br.chatbotplatformskeleton.application.bot.model.BotModel;
import com.br.chatbotplatformskeleton.application.port.out.BotPersistencePort;
import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BotPersistenceAdapter implements BotPersistencePort {

    private final BotRepository botRepository;

    public BotPersistenceAdapter(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    @Override
    public List<BotModel> findAll() {
        return botRepository.findAll().stream()
            .map(this::toModel)
            .toList();
    }

    @Override
    public Optional<BotModel> findById(Long id) {
        return botRepository.findById(id).map(this::toModel);
    }

    @Override
    public boolean existsByKeyIgnoreCase(String key) {
        return botRepository.existsByKeyIgnoreCase(key);
    }

    @Override
    public boolean existsByKeyIgnoreCaseAndIdNot(String key, Long id) {
        return botRepository.existsByKeyIgnoreCaseAndIdNot(key, id);
    }

    @Override
    public BotModel save(BotModel bot) {
        Bot entity = bot.getId() == null
            ? new Bot()
            : botRepository.findById(bot.getId()).orElseGet(Bot::new);

        entity.setName(bot.getName());
        entity.setKey(bot.getKey());
        entity.setEnabled(bot.getEnabled());
        entity.setConfig(bot.getConfig());

        return toModel(botRepository.save(entity));
    }

    private BotModel toModel(Bot entity) {
        BotModel model = new BotModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setKey(entity.getKey());
        model.setEnabled(entity.getEnabled());
        model.setConfig(entity.getConfig());
        return model;
    }
}
