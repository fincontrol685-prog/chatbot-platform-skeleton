package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ConversationDto;
import com.br.chatbotplatformskeleton.service.ConversationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<ConversationDto> createConversation(@RequestBody ConversationDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        ConversationDto created = conversationService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/conversations/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<ConversationDto> getConversation(@PathVariable Long id) {
        return conversationService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bot/{botId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<ConversationDto>> listByBot(@PathVariable Long botId, Pageable pageable) {
        return ResponseEntity.ok(conversationService.findByBotId(botId, pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<ConversationDto>> listByUser(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(conversationService.findByUserId(userId, pageable));
    }

    @GetMapping("/bot/{botId}/active")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<ConversationDto>> listActiveByBot(@PathVariable Long botId, Pageable pageable) {
        return ResponseEntity.ok(conversationService.findActiveConversations(botId, pageable));
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConversationDto> closeConversation(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        return conversationService.closeConversation(id, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/title")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConversationDto> updateTitle(@PathVariable Long id, @RequestParam String title, Authentication authentication) {
        Long userId = extractUserId(authentication);
        return conversationService.updateTitle(id, title, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bot/{botId}/count")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Long> getActiveConversationCount(@PathVariable Long botId) {
        return ResponseEntity.ok(conversationService.getActiveConversationCount(botId));
    }

    private Long extractUserId(Authentication authentication) {
        // This is a simplified version; adjust based on your security setup
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // You might need to query the database to get the user ID from username
            // For now, returning a placeholder; implement according to your UserRepository
            return 1L; // Replace with actual logic
        }
        return 1L;
    }
}

