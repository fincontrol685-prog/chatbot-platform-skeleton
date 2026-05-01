package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.ConversationDto;
import com.br.chatbotplatformskeleton.mapper.ConversationMapper;
import com.br.chatbotplatformskeleton.repository.ConversationRepository;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class ConversationService {

    private static final Logger log = LoggerFactory.getLogger(ConversationService.class);
    private final ConversationRepository conversationRepository;
    private final BotRepository botRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ConversationMapper conversationMapper;

    public ConversationService(ConversationRepository conversationRepository,
                             BotRepository botRepository,
                             UserRepository userRepository,
                             AuditService auditService,
                             ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.botRepository = botRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.conversationMapper = conversationMapper;
    }

    @Transactional
    public ConversationDto create(ConversationDto dto, Long userId) {
        log.info("Creating conversation for user: {} and bot: {}", userId, dto.getBotId());
        Optional<Bot> botOpt = botRepository.findById(dto.getBotId());
        Optional<UserAccount> userOpt = userRepository.findById(userId);

        if (botOpt.isEmpty() || userOpt.isEmpty()) {
            log.warn("Conversation creation failed: bot or user not found - botId: {}, userId: {}", dto.getBotId(), userId);
            throw new IllegalArgumentException("Bot or User not found");
        }

        Conversation conversation = new Conversation();
        conversation.setBot(botOpt.get());
        conversation.setUser(userOpt.get());
        String title = dto.getTitle();
        if (title == null || title.isBlank()) {
            title = "Conversa " + System.currentTimeMillis();
        }
        conversation.setTitle(title);
        conversation.setStatus("ACTIVE");
        conversation.setMessageCount(0L);
        conversation.setMetadata(dto.getMetadata());

        Conversation saved = conversationRepository.save(conversation);
        auditService.log(userId, "CREATE", "CONVERSATION", saved.getId(), null, toJson(saved));
        log.info("Conversation created successfully with id: {}", saved.getId());
        return conversationMapper.toDto(saved);
    }

    public Optional<ConversationDto> findById(Long id) {
        log.debug("Finding conversation with id: {}", id);
        return conversationRepository.findById(id).map(conversationMapper::toDto);
    }

    public Page<ConversationDto> findByBotId(Long botId, Pageable pageable) {
        log.debug("Finding conversations for bot: {}", botId);
        return conversationRepository.findByBotId(botId, pageable).map(conversationMapper::toDto);
    }

    public Page<ConversationDto> findByUserId(Long userId, Pageable pageable) {
        log.debug("Finding conversations for user: {}", userId);
        return conversationRepository.findByUserId(userId, pageable).map(conversationMapper::toDto);
    }

    public Page<ConversationDto> findByBotIdAndStatus(Long botId, String status, Pageable pageable) {
        log.debug("Finding conversations with status: {} for bot: {}", status, botId);
        return conversationRepository.findByBotIdAndStatus(botId, status, pageable).map(conversationMapper::toDto);
    }

    public Page<ConversationDto> findActiveConversations(Long botId, Pageable pageable) {
        return findByBotIdAndStatus(botId, "ACTIVE", pageable);
    }

    @Transactional
    public Optional<ConversationDto> closeConversation(Long conversationId, Long userId) {
        log.info("Closing conversation: {} by user: {}", conversationId, userId);
        Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
        if (convOpt.isEmpty()) {
            log.warn("Close conversation failed: conversation not found - id: {}", conversationId);
            return Optional.empty();
        }

        Conversation conversation = convOpt.get();
        String oldValue = toJson(conversation);
        conversation.setStatus("CLOSED");
        conversation.setClosedAt(OffsetDateTime.now());

        Conversation saved = conversationRepository.save(conversation);
        auditService.log(userId, "UPDATE", "CONVERSATION", saved.getId(), oldValue, toJson(saved));
        return Optional.of(conversationMapper.toDto(saved));
    }

    @Transactional
    public Optional<ConversationDto> updateTitle(Long conversationId, String newTitle, Long userId) {
        log.info("Updating conversation title for id: {} by user: {}", conversationId, userId);
        Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
        if (convOpt.isEmpty()) {
            log.warn("Update title failed: conversation not found - id: {}", conversationId);
            return Optional.empty();
        }

        Conversation conversation = convOpt.get();
        String oldValue = conversation.getTitle();
        conversation.setTitle(newTitle);
        conversation.setUpdatedAt(OffsetDateTime.now());

        Conversation saved = conversationRepository.save(conversation);
        auditService.log(userId, "UPDATE", "CONVERSATION", saved.getId(), oldValue, newTitle);
        return Optional.of(conversationMapper.toDto(saved));
    }

    public long getActiveConversationCount(Long botId) {
        return conversationRepository.countActiveByBotId(botId);
    }

    public Page<ConversationDto> searchConversations(Long botId, String status, OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable) {
        if (status != null && startDate != null && endDate != null) {
            return conversationRepository.findByBotIdAndDateRange(botId, startDate, endDate, pageable).map(conversationMapper::toDto);
        }
        return findByBotId(botId, pageable);
    }


    private String toJson(Conversation conversation) {
        return String.format("{\"id\":%d,\"title\":\"%s\",\"status\":\"%s\",\"messageCount\":%d}",
            conversation.getId(), conversation.getTitle(), conversation.getStatus(), conversation.getMessageCount());
    }
}

