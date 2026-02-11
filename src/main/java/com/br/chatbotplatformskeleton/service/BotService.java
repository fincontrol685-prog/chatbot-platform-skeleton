package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.dto.BotDto;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BotService {

    private final BotRepository botRepository;

    public BotService(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    public List<BotDto> listAll() {
        return botRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<BotDto> findById(Long id) {
        return botRepository.findById(id).map(this::toDto);
    }

    public BotDto create(BotDto dto) {
        Bot b = new Bot();
        b.setName(dto.getName());
        b.setKey(dto.getKey());
        b.setEnabled(dto.getEnabled() == null ? true : dto.getEnabled());
        b.setConfig(dto.getConfig());
        Bot saved = botRepository.save(b);
        return toDto(saved);
    }

    public Optional<BotDto> activate(Long id, boolean active) {
        Optional<Bot> ob = botRepository.findById(id);
        if (ob.isEmpty()) return Optional.empty();
        Bot b = ob.get();
        b.setEnabled(active);
        Bot saved = botRepository.save(b);
        return Optional.of(toDto(saved));
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
}
