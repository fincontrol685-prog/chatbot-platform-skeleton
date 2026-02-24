package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.NotificationDto;
import com.br.chatbotplatformskeleton.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<NotificationDto>> listByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.listByUser(userId, pageable));
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<NotificationDto>> listUnreadByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.listUnreadByUser(userId, pageable));
    }

    @GetMapping("/user/{userId}/unread-list")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Long> countUnreadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.countUnreadByUser(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<NotificationDto> getNotification(@PathVariable Long id) {
        return notificationService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/mark-as-read")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/user/{userId}/mark-all-as-read")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<NotificationDto>> listByType(
            @PathVariable Long userId,
            @PathVariable String type,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.listByType(userId, type, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}

