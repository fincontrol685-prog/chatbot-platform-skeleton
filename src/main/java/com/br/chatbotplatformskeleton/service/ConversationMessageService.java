package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.domain.ConversationMessage;
import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.ConversationExchangeDto;
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
    private final BotResponseService botResponseService;

    public ConversationMessageService(ConversationMessageRepository messageRepository,
                                     ConversationRepository conversationRepository,
                                     UserRepository userRepository,
                                     AuditService auditService,
                                     BotResponseService botResponseService) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.botResponseService = botResponseService;
    }

    public ConversationMessageDto addMessage(ConversationMessageDto dto, Long conversationId, Long senderId) {
        Conversation conversation = requireConversation(conversationId);
        UserAccount sender = requireUser(senderId);

        ConversationMessage saved = saveMessage(
            conversation,
            sender,
            normalizeMessageType(dto.getMessageType()),
            normalizeContent(dto.getContent()),
            dto.getSentimentScore(),
            dto.getIntent(),
            dto.getConfidence(),
            dto.getResponseTimeMs(),
            dto.getMetadata(),
            Boolean.TRUE.equals(dto.getIsFlagged())
        );

        touchConversation(conversation, 1L);
        auditService.log(senderId, "CREATE", "MESSAGE", saved.getId(), null, saved.getContent());
        return toDto(saved);
    }

    public ConversationExchangeDto processUserMessage(ConversationMessageDto dto, Long conversationId, Long senderId) {
        Conversation conversation = requireConversation(conversationId);
        if (!"ACTIVE".equalsIgnoreCase(conversation.getStatus())) {
            throw new IllegalArgumentException("A conversa nao esta ativa");
        }

        UserAccount sender = requireUser(senderId);
        String content = normalizeContent(dto.getContent());
        BotResponseService.ResponsePlan responsePlan = botResponseService.buildResponsePlan(
            conversation,
            messageRepository.findMessagesByConversationIdOrdered(conversationId),
            content
        );

        ConversationMessage userMessage = saveMessage(
            conversation,
            sender,
            "USER",
            content,
            responsePlan.sentimentScore(),
            responsePlan.intent(),
            responsePlan.confidence(),
            null,
            responsePlan.metadata(),
            responsePlan.flaggedForReview()
        );

        UserAccount botSender = userRepository.findByUsernameIgnoreCase(botResponseService.getSystemUsername())
            .orElseThrow(() -> new IllegalArgumentException("Usuario interno do bot nao encontrado"));

        ConversationMessage botMessage = saveMessage(
            conversation,
            botSender,
            "BOT",
            responsePlan.botContent(),
            Math.max(0.05d, responsePlan.sentimentScore()),
            responsePlan.intent(),
            responsePlan.confidence(),
            responsePlan.responseTimeMs(),
            responsePlan.metadata(),
            false
        );

        touchConversation(conversation, 2L);
        auditService.log(senderId, "CREATE", "MESSAGE", userMessage.getId(), null, userMessage.getContent());
        auditService.log(senderId, "CREATE", "BOT_MESSAGE", botMessage.getId(), null, botMessage.getContent());

        ConversationExchangeDto exchange = new ConversationExchangeDto();
        exchange.setUserMessage(toDto(userMessage));
        exchange.setBotMessage(toDto(botMessage));
        return exchange;
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
        dto.setSenderUsername(resolveSenderUsername(message));
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

    private Conversation requireConversation(Long conversationId) {
        return conversationRepository.findById(conversationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
    }

    private UserAccount requireUser(Long senderId) {
        return userRepository.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private ConversationMessage saveMessage(
        Conversation conversation,
        UserAccount sender,
        String messageType,
        String content,
        Double sentimentScore,
        String intent,
        Double confidence,
        Long responseTimeMs,
        String metadata,
        boolean flagged
    ) {
        ConversationMessage message = new ConversationMessage();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setSentimentScore(sentimentScore);
        message.setIntent(intent);
        message.setConfidence(confidence);
        message.setResponseTimeMs(responseTimeMs);
        message.setMetadata(metadata);
        message.setIsFlagged(flagged);
        return messageRepository.save(message);
    }

    private void touchConversation(Conversation conversation, long increment) {
        conversation.setMessageCount(conversation.getMessageCount() + increment);
        conversation.setUpdatedAt(OffsetDateTime.now());
        conversationRepository.save(conversation);
    }

    private String normalizeContent(String content) {
        if (content == null || content.trim().isBlank()) {
            throw new IllegalArgumentException("Mensagem e obrigatoria");
        }
        return content.trim();
    }

    private String normalizeMessageType(String messageType) {
        if (messageType == null || messageType.isBlank()) {
            return "USER";
        }
        return messageType.trim().toUpperCase();
    }

    private String resolveSenderUsername(ConversationMessage message) {
        if ("BOT".equalsIgnoreCase(message.getMessageType())) {
            Bot bot = message.getConversation().getBot();
            return bot != null ? bot.getName() : "Bot";
        }
        return message.getSender().getUsername();
    }
}
