package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ConversationDto;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.service.CurrentUserService;
import com.br.chatbotplatformskeleton.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/conversations")
@Tag(name = "Conversations", description = "Gerenciamento de Conversas")
@SecurityRequirement(name = "Bearer Authentication")
public class ConversationController {

    private final ConversationService conversationService;
    private final CurrentUserService currentUserService;

    public ConversationController(ConversationService conversationService, CurrentUserService currentUserService) {
        this.conversationService = conversationService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Criar Conversa", description = "Inicia uma nova conversa com um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationDto> createConversation(@RequestBody ConversationDto dto, Authentication authentication) {
        Long userId = currentUserService.requireCurrentUserId(authentication);
        ConversationDto created = conversationService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/conversations/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Conversa por ID", description = "Retorna os detalhes de uma conversa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversa encontrada"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationDto> getConversation(@PathVariable Long id, Authentication authentication) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        return conversationService.findById(id, currentUser)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bot/{botId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Listar Conversas por Bot", description = "Retorna todas as conversas de um bot específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de conversas obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<ConversationDto>> listByBot(@PathVariable Long botId, Pageable pageable) {
        return ResponseEntity.ok(conversationService.findByBotId(botId, pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Conversas por Usuário", description = "Retorna todas as conversas de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de conversas obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<ConversationDto>> listByUser(@PathVariable Long userId, Pageable pageable, Authentication authentication) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        return ResponseEntity.ok(conversationService.findByUserId(userId, pageable, currentUser));
    }

    @GetMapping("/bot/{botId}/active")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Listar Conversas Ativas por Bot", description = "Retorna todas as conversas ativas de um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de conversas ativas obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<ConversationDto>> listActiveByBot(@PathVariable Long botId, Pageable pageable) {
        return ResponseEntity.ok(conversationService.findActiveConversations(botId, pageable));
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Fechar Conversa", description = "Encerra uma conversa ativa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversa fechada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationDto> closeConversation(@PathVariable Long id, Authentication authentication) {
        Long userId = currentUserService.requireCurrentUserId(authentication);
        return conversationService.closeConversation(id, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/title")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atualizar Título da Conversa", description = "Altera o título de uma conversa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Título atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationDto> updateTitle(@PathVariable Long id, @RequestParam String title, Authentication authentication) {
        Long userId = currentUserService.requireCurrentUserId(authentication);
        return conversationService.updateTitle(id, title, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bot/{botId}/count")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Contar Conversas Ativas", description = "Retorna a quantidade de conversas ativas de um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Long> getActiveConversationCount(@PathVariable Long botId) {
        return ResponseEntity.ok(conversationService.getActiveConversationCount(botId));
    }
}
