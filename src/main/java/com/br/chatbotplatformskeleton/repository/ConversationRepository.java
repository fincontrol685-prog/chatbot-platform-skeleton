package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    
    @Query("SELECT DISTINCT c FROM Conversation c LEFT JOIN FETCH c.bot LEFT JOIN FETCH c.user WHERE c.bot.id = :botId")
    Page<Conversation> findByBotId(@Param("botId") Long botId, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Conversation c LEFT JOIN FETCH c.bot LEFT JOIN FETCH c.user WHERE c.user.id = :userId")
    Page<Conversation> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Conversation c LEFT JOIN FETCH c.bot LEFT JOIN FETCH c.user WHERE c.bot.id = :botId AND c.status = :status")
    Page<Conversation> findByBotIdAndStatus(@Param("botId") Long botId, @Param("status") String status, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Conversation c LEFT JOIN FETCH c.bot LEFT JOIN FETCH c.user WHERE c.user.id = :userId AND c.status = :status")
    Page<Conversation> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Conversation c LEFT JOIN FETCH c.bot LEFT JOIN FETCH c.user WHERE c.bot.id = :botId AND c.createdAt >= :startDate AND c.createdAt <= :endDate")
    Page<Conversation> findByBotIdAndDateRange(@Param("botId") Long botId,
                                                @Param("startDate") OffsetDateTime startDate,
                                                @Param("endDate") OffsetDateTime endDate,
                                                Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Conversation c WHERE c.bot.id = :botId")
    long countByBotId(@Param("botId") Long botId);

    @Query("SELECT COUNT(c) FROM Conversation c WHERE c.bot.id = :botId AND c.status = 'ACTIVE'")
    long countActiveByBotId(@Param("botId") Long botId);
    
    @Query("SELECT COUNT(c) FROM Conversation c WHERE c.user.id = :userId AND c.status = 'ACTIVE'")
    long countActiveByUserId(@Param("userId") Long userId);
    
    @Query("SELECT DISTINCT c FROM Conversation c LEFT JOIN FETCH c.bot LEFT JOIN FETCH c.user WHERE c.status = :status AND c.closedAt IS NOT NULL")
    List<Conversation> findByStatusAndClosedAtNotNull(@Param("status") String status);
}

