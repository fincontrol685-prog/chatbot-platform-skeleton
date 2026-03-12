package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.ConversationDto;
import com.br.chatbotplatformskeleton.repository.ConversationRepository;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final BotRepository botRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public ConversationService(ConversationRepository conversationRepository,
                             BotRepository botRepository,
                             UserRepository userRepository,
                             AuditService auditService) {
        this.conversationRepository = conversationRepository;
        this.botRepository = botRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public ConversationDto create(ConversationDto dto, Long userId) {
        Optional<Bot> botOpt = botRepository.findById(dto.getBotId());
        Optional<UserAccount> userOpt = userRepository.findById(userId);

        if (botOpt.isEmpty() || userOpt.isEmpty()) {
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
        return toDto(saved);
    }

    public Optional<ConversationDto> findById(Long id) {
        return conversationRepository.findById(id).map(this::toDto);
    }

    public Page<ConversationDto> findByBotId(Long botId, Pageable pageable) {
        return conversationRepository.findByBotId(botId, pageable).map(this::toDto);
    }

    public Page<ConversationDto> findByUserId(Long userId, Pageable pageable) {
        return conversationRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    public Page<ConversationDto> findByBotIdAndStatus(Long botId, String status, Pageable pageable) {
        return conversationRepository.findByBotIdAndStatus(botId, status, pageable).map(this::toDto);
    }

    public Page<ConversationDto> findActiveConversations(Long botId, Pageable pageable) {
        return findByBotIdAndStatus(botId, "ACTIVE", pageable);
    }

    public Optional<ConversationDto> closeConversation(Long conversationId, Long userId) {
        Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
        if (convOpt.isEmpty()) return Optional.empty();

        Conversation conversation = convOpt.get();
        String oldValue = toJson(conversation);
        conversation.setStatus("CLOSED");
        conversation.setClosedAt(OffsetDateTime.now());

        Conversation saved = conversationRepository.save(conversation);
        auditService.log(userId, "UPDATE", "CONVERSATION", saved.getId(), oldValue, toJson(saved));
        return Optional.of(toDto(saved));
    }

    public Optional<ConversationDto> updateTitle(Long conversationId, String newTitle, Long userId) {
        Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
        if (convOpt.isEmpty()) return Optional.empty();

        Conversation conversation = convOpt.get();
        String oldValue = conversation.getTitle();
        conversation.setTitle(newTitle);
        conversation.setUpdatedAt(OffsetDateTime.now());

        Conversation saved = conversationRepository.save(conversation);
        auditService.log(userId, "UPDATE", "CONVERSATION", saved.getId(), oldValue, newTitle);
        return Optional.of(toDto(saved));
    }

    public long getActiveConversationCount(Long botId) {
        return conversationRepository.countActiveByBotId(botId);
    }

    public Page<ConversationDto> searchConversations(Long botId, String status, OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable) {
        if (status != null && startDate != null && endDate != null) {
            return conversationRepository.findByBotIdAndDateRange(botId, startDate, endDate, pageable).map(this::toDto);
        }
        return findByBotId(botId, pageable);
    }

    private ConversationDto toDto(Conversation conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());
        dto.setBotId(conversation.getBot().getId());
        dto.setUserId(conversation.getUser().getId());
        dto.setTitle(conversation.getTitle());
        dto.setStatus(conversation.getStatus());
        dto.setMessageCount(conversation.getMessageCount());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        dto.setClosedAt(conversation.getClosedAt());
        dto.setMetadata(conversation.getMetadata());
        return dto;
    }

    private String toJson(Conversation conversation) {
        return String.format("{\"id\":%d,\"title\":\"%s\",\"status\":\"%s\",\"messageCount\":%d}",
            conversation.getId(), conversation.getTitle(), conversation.getStatus(), conversation.getMessageCount());
    }
}

