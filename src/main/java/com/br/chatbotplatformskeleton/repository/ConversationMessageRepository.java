package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.ConversationMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {
    
    Page<ConversationMessage> findByConversationId(Long conversationId, Pageable pageable);
    
    Page<ConversationMessage> findByConversationIdAndMessageType(Long conversationId, String messageType, Pageable pageable);
    
    @Query("SELECT m FROM ConversationMessage m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt ASC")
    List<ConversationMessage> findMessagesByConversationIdOrdered(@Param("conversationId") Long conversationId);
    
    @Query("SELECT COUNT(m) FROM ConversationMessage m WHERE m.conversation.id = :conversationId")
    long countByConversationId(@Param("conversationId") Long conversationId);

    @Query("SELECT COUNT(m) FROM ConversationMessage m WHERE m.conversation.bot.id = :botId")
    long countByBotId(@Param("botId") Long botId);
    
    @Query("SELECT COUNT(m) FROM ConversationMessage m WHERE m.conversation.bot.id = :botId AND m.createdAt >= :startDate AND m.createdAt <= :endDate")
    long countMessagesByBotAndDateRange(@Param("botId") Long botId,
                                        @Param("startDate") OffsetDateTime startDate,
                                        @Param("endDate") OffsetDateTime endDate);
    
    @Query("SELECT m FROM ConversationMessage m WHERE m.isFlagged = true AND m.conversation.bot.id = :botId")
    Page<ConversationMessage> findFlaggedMessages(@Param("botId") Long botId, Pageable pageable);
    
    @Query("SELECT AVG(m.responseTimeMs) FROM ConversationMessage m WHERE m.conversation.bot.id = :botId AND m.messageType = 'BOT'")
    Double getAverageResponseTime(@Param("botId") Long botId);
    
    @Query("SELECT AVG(m.sentimentScore) FROM ConversationMessage m WHERE m.conversation.bot.id = :botId")
    Double getAverageSentimentScore(@Param("botId") Long botId);
}
