package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.ConsentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentLogRepository extends JpaRepository<ConsentLog, Long> {

    List<ConsentLog> findByUserIdAndConsentType(Long userId, String consentType);

    Page<ConsentLog> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT c FROM ConsentLog c WHERE c.user.id = :userId AND c.consentType = :type AND c.withdrawnAt IS NULL ORDER BY c.createdAt DESC")
    List<ConsentLog> findActiveConsentsByUserAndType(@Param("userId") Long userId, @Param("type") String type);

    @Query("SELECT c FROM ConsentLog c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    List<ConsentLog> findAllByUserId(@Param("userId") Long userId);
}

