package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<AuditLog> findByUserId(Long userId, Pageable pageable);

    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    Page<AuditLog> findByEntityId(Long entityId, Pageable pageable);

    Page<AuditLog> findByAction(String action, Pageable pageable);

    Page<AuditLog> findByStatus(String status, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId AND a.createdAt >= :startDate AND a.createdAt <= :endDate")
    Page<AuditLog> findByUserIdAndDateRange(@Param("userId") Long userId,
                                           @Param("startDate") OffsetDateTime startDate,
                                           @Param("endDate") OffsetDateTime endDate,
                                           Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.status = 'FAILED' ORDER BY a.createdAt DESC")
    Page<AuditLog> findFailedAuditLogs(Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.createdAt DESC")
    List<AuditLog> findAuditTrailForEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId);
}
