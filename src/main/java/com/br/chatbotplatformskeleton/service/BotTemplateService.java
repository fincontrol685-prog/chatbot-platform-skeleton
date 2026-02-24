package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.BotTemplate;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.BotTemplateDto;
import com.br.chatbotplatformskeleton.repository.BotTemplateRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BotTemplateService {

    private final BotTemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public BotTemplateService(BotTemplateRepository templateRepository,
                             UserRepository userRepository,
                             AuditService auditService) {
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public BotTemplateDto create(BotTemplateDto dto, Long userId) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        BotTemplate template = new BotTemplate();
        template.setName(dto.getName());
        template.setDescription(dto.getDescription());
        template.setConfig(dto.getConfig());
        template.setCategory(dto.getCategory());
        template.setIconUrl(dto.getIconUrl());
        template.setCreatedBy(userOpt.get());
        template.setIsPublic(dto.getIsPublic() != null ? dto.getIsPublic() : false);
        template.setUsageCount(0L);

        BotTemplate saved = templateRepository.save(template);
        auditService.log(userId, "CREATE", "BOT_TEMPLATE", saved.getId(), null, saved.getName());
        return toDto(saved);
    }

    public Optional<BotTemplateDto> findById(Long id) {
        return templateRepository.findById(id).map(this::toDto);
    }

    public Page<BotTemplateDto> listPublic(Pageable pageable) {
        return templateRepository.findByIsPublicTrue(pageable).map(this::toDto);
    }

    public Page<BotTemplateDto> listByCategory(String category, Pageable pageable) {
        return templateRepository.findByCategory(category, pageable).map(this::toDto);
    }

    public Page<BotTemplateDto> listByUser(Long userId, Pageable pageable) {
        return templateRepository.findByCreatedById(userId, pageable).map(this::toDto);
    }

    public Page<BotTemplateDto> listMostUsed(Pageable pageable) {
        return templateRepository.findMostUsedPublicTemplates(pageable).map(this::toDto);
    }

    public Page<BotTemplateDto> listTopRated(Pageable pageable) {
        return templateRepository.findTopRatedTemplates(pageable).map(this::toDto);
    }

    public Optional<BotTemplateDto> updateTemplate(Long id, BotTemplateDto dto, Long userId) {
        Optional<BotTemplate> opt = templateRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        BotTemplate template = opt.get();
        String oldValue = toJson(template);

        template.setName(dto.getName());
        template.setDescription(dto.getDescription());
        template.setConfig(dto.getConfig());
        template.setCategory(dto.getCategory());
        template.setIconUrl(dto.getIconUrl());
        template.setIsPublic(dto.getIsPublic());
        template.setUpdatedAt(OffsetDateTime.now());

        BotTemplate saved = templateRepository.save(template);
        auditService.log(userId, "UPDATE", "BOT_TEMPLATE", saved.getId(), oldValue, toJson(saved));
        return Optional.of(toDto(saved));
    }

    public void incrementUsage(Long templateId) {
        Optional<BotTemplate> opt = templateRepository.findById(templateId);
        if (opt.isPresent()) {
            BotTemplate template = opt.get();
            template.setUsageCount(template.getUsageCount() + 1);
            templateRepository.save(template);
        }
    }

    public Optional<BotTemplateDto> rateTemplate(Long id, Double rating) {
        Optional<BotTemplate> opt = templateRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        BotTemplate template = opt.get();
        template.setRating(rating);
        BotTemplate saved = templateRepository.save(template);
        return Optional.of(toDto(saved));
    }

    private BotTemplateDto toDto(BotTemplate template) {
        BotTemplateDto dto = new BotTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setConfig(template.getConfig());
        dto.setCategory(template.getCategory());
        dto.setIconUrl(template.getIconUrl());
        dto.setCreatedById(template.getCreatedBy().getId());
        dto.setCreatedByUsername(template.getCreatedBy().getUsername());
        dto.setIsPublic(template.getIsPublic());
        dto.setUsageCount(template.getUsageCount());
        dto.setRating(template.getRating());
        dto.setCreatedAt(template.getCreatedAt());
        dto.setUpdatedAt(template.getUpdatedAt());
        return dto;
    }

    private String toJson(BotTemplate template) {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"category\":\"%s\"}",
            template.getId(), template.getName(), template.getCategory());
    }
}

