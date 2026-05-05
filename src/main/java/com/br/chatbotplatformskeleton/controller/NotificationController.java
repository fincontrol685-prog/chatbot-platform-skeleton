package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.NotificationDto;
import com.br.chatbotplatformskeleton.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Gerenciamento de Notificações")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Notificações do Usuário", description = "Retorna as notificações de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<NotificationDto>> listByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.listByUser(userId, pageable));
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Notificações Não Lidas", description = "Retorna as notificações não lidas do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações não lidas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<NotificationDto>> listUnreadByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.listUnreadByUser(userId, pageable));
    }

    @GetMapping("/user/{userId}/unread-list")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Lista de Não Lidas", description = "Retorna todas as notificações não lidas em forma de lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações não lidas obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Contar Notificações Não Lidas", description = "Retorna a quantidade de notificações não lidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Long> countUnreadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.countUnreadByUser(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Notificação por ID", description = "Retorna os detalhes de uma notificação específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<NotificationDto> getNotification(@PathVariable Long id) {
        return notificationService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/mark-as-read")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Marcar Notificação como Lida", description = "Marca uma notificação específica como lida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação marcada como lida"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/user/{userId}/mark-all-as-read")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Marcar Todas como Lidas", description = "Marca todas as notificações do usuário como lidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todas as notificações marcadas como lidas"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Notificações por Tipo", description = "Retorna notificações filtradas por tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<NotificationDto>> listByType(
            @PathVariable Long userId,
            @PathVariable String type,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.listByType(userId, type, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Deletar Notificação", description = "Remove uma notificação específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificação deletada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}

