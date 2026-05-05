package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.AuditLogDto;
import com.br.chatbotplatformskeleton.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@Tag(name = "Audit Logs", description = "Registros de Auditoria e Trilha de Eventos")
@SecurityRequirement(name = "Bearer Authentication")
public class AuditLogController {

    private final AuditService auditService;

    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Listar Registros de Auditoria", description = "Lista registros de auditoria com filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<AuditLogDto>> listAuditLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.search(action, entityType, status, pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Auditoria por Usuário", description = "Retorna todos os registros de auditoria de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<AuditLogDto>> getAuditLogsByUser(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(auditService.findByUserId(userId, pageable));
    }

    @GetMapping("/entity-type/{entityType}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Auditoria por Tipo de Entidade", description = "Retorna registros de auditoria filtrados por tipo de entidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<AuditLogDto>> getAuditLogsByEntityType(@PathVariable String entityType, Pageable pageable) {
        return ResponseEntity.ok(auditService.findByEntityType(entityType, pageable));
    }

    @GetMapping("/entity/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Auditoria por ID da Entidade", description = "Retorna todas as operações executadas em uma entidade específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<AuditLogDto>> getAuditLogsByEntityId(@PathVariable Long entityId, Pageable pageable) {
        return ResponseEntity.ok(auditService.findByEntityId(entityId, pageable));
    }

    @GetMapping("/failed")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Operações Falhadas", description = "Retorna todos os registros de auditoria com falha (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<AuditLogDto>> getFailedAuditLogs(Pageable pageable) {
        return ResponseEntity.ok(auditService.findFailedAuditLogs(pageable));
    }

    @GetMapping("/trail/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Trilha de Auditoria", description = "Retorna a trilha completa de alterações de uma entidade específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trilha obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<AuditLogDto>> getAuditTrail(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getAuditTrailForEntity(entityType, entityId));
    }
}
