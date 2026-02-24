package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ConversationMessageDto;
import com.br.chatbotplatformskeleton.service.ConversationMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ConversationMessageController {

    private final ConversationMessageService messageService;

    public ConversationMessageController(ConversationMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/conversation/{conversationId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<ConversationMessageDto> addMessage(
            @PathVariable Long conversationId,
            @RequestBody ConversationMessageDto dto,
            Authentication authentication) {
        Long senderId = extractUserId(authentication);
        ConversationMessageDto created = messageService.addMessage(dto, conversationId, senderId);
        return ResponseEntity.created(URI.create("/api/messages/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<ConversationMessageDto> getMessage(@PathVariable Long id) {
        return messageService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/conversation/{conversationId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<ConversationMessageDto>> listByConversation(
            @PathVariable Long conversationId,
            Pageable pageable) {
        return ResponseEntity.ok(messageService.findByConversationId(conversationId, pageable));
    }

    @GetMapping("/conversation/{conversationId}/history")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<ConversationMessageDto>> getConversationHistory(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getConversationHistory(conversationId));
    }

    @PatchMapping("/{id}/flag")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConversationMessageDto> flagMessage(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        return messageService.flagMessage(id, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bot/{botId}/flagged")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Page<ConversationMessageDto>> listFlaggedMessages(
            @PathVariable Long botId,
            Pageable pageable) {
        return ResponseEntity.ok(messageService.findFlaggedMessages(botId, pageable));
    }

    @GetMapping("/bot/{botId}/stats/avg-response-time")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Double> getAverageResponseTime(@PathVariable Long botId) {
        return ResponseEntity.ok(messageService.getAverageResponseTime(botId));
    }

    @GetMapping("/bot/{botId}/stats/avg-sentiment")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Double> getAverageSentimentScore(@PathVariable Long botId) {
        return ResponseEntity.ok(messageService.getAverageSentimentScore(botId));
    }

    private Long extractUserId(Authentication authentication) {
        // Placeholder implementation
        return 1L;
    }
}

