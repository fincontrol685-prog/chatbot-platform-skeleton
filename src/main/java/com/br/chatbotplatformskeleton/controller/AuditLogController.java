package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.AuditLogDto;
import com.br.chatbotplatformskeleton.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditService auditService;

    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Page<AuditLogDto>> getAuditLogsByUser(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(auditService.findByUserId(userId, pageable));
    }

    @GetMapping("/entity-type/{entityType}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Page<AuditLogDto>> getAuditLogsByEntityType(@PathVariable String entityType, Pageable pageable) {
        return ResponseEntity.ok(auditService.findByEntityType(entityType, pageable));
    }

    @GetMapping("/entity/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Page<AuditLogDto>> getAuditLogsByEntityId(@PathVariable Long entityId, Pageable pageable) {
        return ResponseEntity.ok(auditService.findByEntityId(entityId, pageable));
    }

    @GetMapping("/failed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditLogDto>> getFailedAuditLogs(Pageable pageable) {
        return ResponseEntity.ok(auditService.findFailedAuditLogs(pageable));
    }

    @GetMapping("/trail/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<AuditLogDto>> getAuditTrail(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getAuditTrailForEntity(entityType, entityId));
    }
}

