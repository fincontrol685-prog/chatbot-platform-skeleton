package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.ConversationMessage;
import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.ConversationMessageDto;
import com.br.chatbotplatformskeleton.repository.ConversationMessageRepository;
import com.br.chatbotplatformskeleton.repository.ConversationRepository;
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
public class ConversationMessageService {

    private final ConversationMessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public ConversationMessageService(ConversationMessageRepository messageRepository,
                                     ConversationRepository conversationRepository,
                                     UserRepository userRepository,
                                     AuditService auditService) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public ConversationMessageDto addMessage(ConversationMessageDto dto, Long conversationId, Long senderId) {
        Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
        Optional<UserAccount> userOpt = userRepository.findById(senderId);

        if (convOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Conversation or User not found");
        }

        Conversation conversation = convOpt.get();
        UserAccount sender = userOpt.get();

        ConversationMessage message = new ConversationMessage();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageType(dto.getMessageType() != null ? dto.getMessageType() : "USER");
        message.setContent(dto.getContent());
        message.setSentimentScore(dto.getSentimentScore());
        message.setIntent(dto.getIntent());
        message.setConfidence(dto.getConfidence());
        message.setResponseTimeMs(dto.getResponseTimeMs());
        message.setMetadata(dto.getMetadata());
        message.setIsFlagged(false);

        ConversationMessage saved = messageRepository.save(message);

        // Update conversation message count and updated_at
        conversation.setMessageCount(conversation.getMessageCount() + 1);
        conversation.setUpdatedAt(OffsetDateTime.now());
        conversationRepository.save(conversation);

        auditService.log(senderId, "CREATE", "MESSAGE", saved.getId(), null, saved.getContent());
        return toDto(saved);
    }

    public Optional<ConversationMessageDto> findById(Long id) {
        return messageRepository.findById(id).map(this::toDto);
    }

    public Page<ConversationMessageDto> findByConversationId(Long conversationId, Pageable pageable) {
        return messageRepository.findByConversationId(conversationId, pageable).map(this::toDto);
    }

    public List<ConversationMessageDto> getConversationHistory(Long conversationId) {
        return messageRepository.findMessagesByConversationIdOrdered(conversationId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public Optional<ConversationMessageDto> flagMessage(Long messageId, Long userId) {
        Optional<ConversationMessage> msgOpt = messageRepository.findById(messageId);
        if (msgOpt.isEmpty()) return Optional.empty();

        ConversationMessage message = msgOpt.get();
        message.setIsFlagged(true);
        ConversationMessage saved = messageRepository.save(message);

        auditService.log(userId, "UPDATE", "MESSAGE", saved.getId(), "isFlagged=false", "isFlagged=true");
        return Optional.of(toDto(saved));
    }

    public Page<ConversationMessageDto> findFlaggedMessages(Long botId, Pageable pageable) {
        return messageRepository.findFlaggedMessages(botId, pageable).map(this::toDto);
    }

    public Double getAverageResponseTime(Long botId) {
        return messageRepository.getAverageResponseTime(botId);
    }

    public Double getAverageSentimentScore(Long botId) {
        return messageRepository.getAverageSentimentScore(botId);
    }

    public long getTotalMessageCount(Long conversationId) {
        return messageRepository.countByConversationId(conversationId);
    }

    private ConversationMessageDto toDto(ConversationMessage message) {
        ConversationMessageDto dto = new ConversationMessageDto();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setMessageType(message.getMessageType());
        dto.setContent(message.getContent());
        dto.setSentimentScore(message.getSentimentScore());
        dto.setIntent(message.getIntent());
        dto.setConfidence(message.getConfidence());
        dto.setResponseTimeMs(message.getResponseTimeMs());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setMetadata(message.getMetadata());
        dto.setIsFlagged(message.getIsFlagged());
        return dto;
    }
}

