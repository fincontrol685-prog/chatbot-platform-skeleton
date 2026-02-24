package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.AuditLog;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.AuditLogDto;
import com.br.chatbotplatformskeleton.repository.AuditLogRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public void log(Long userId, String action, String entityType, Long entityId, String oldValue, String newValue) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return;

        AuditLog auditLog = new AuditLog();
        auditLog.setUser(userOpt.get());
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setIpAddress(getClientIpAddress());
        auditLog.setStatus("SUCCESS");
        auditLog.setCreatedAt(OffsetDateTime.now());

        auditLogRepository.save(auditLog);
    }

    public void logError(Long userId, String action, String entityType, Long entityId, String errorMessage) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return;

        AuditLog auditLog = new AuditLog();
        auditLog.setUser(userOpt.get());
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setStatus("FAILED");
        auditLog.setErrorMessage(errorMessage);
        auditLog.setIpAddress(getClientIpAddress());
        auditLog.setCreatedAt(OffsetDateTime.now());

        auditLogRepository.save(auditLog);
    }

    public Page<AuditLogDto> findByUserId(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    public Page<AuditLogDto> findByEntityType(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityType(entityType, pageable).map(this::toDto);
    }

    public Page<AuditLogDto> findByEntityId(Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityId(entityId, pageable).map(this::toDto);
    }

    public Page<AuditLogDto> findFailedAuditLogs(Pageable pageable) {
        return auditLogRepository.findFailedAuditLogs(pageable).map(this::toDto);
    }

    public List<AuditLogDto> getAuditTrailForEntity(String entityType, Long entityId) {
        return auditLogRepository.findAuditTrailForEntity(entityType, entityId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            // Fallback if not in HTTP context
        }
        return "UNKNOWN";
    }

    private AuditLogDto toDto(AuditLog auditLog) {
        AuditLogDto dto = new AuditLogDto();
        dto.setId(auditLog.getId());
        dto.setUserId(auditLog.getUser().getId());
        dto.setUsername(auditLog.getUser().getUsername());
        dto.setAction(auditLog.getAction());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setOldValue(auditLog.getOldValue());
        dto.setNewValue(auditLog.getNewValue());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setStatus(auditLog.getStatus());
        dto.setErrorMessage(auditLog.getErrorMessage());
        dto.setCreatedAt(auditLog.getCreatedAt());
        return dto;
    }
}

