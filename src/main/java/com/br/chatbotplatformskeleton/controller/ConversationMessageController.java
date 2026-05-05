package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ConversationExchangeDto;
import com.br.chatbotplatformskeleton.dto.ConversationMessageDto;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.service.CurrentUserService;
import com.br.chatbotplatformskeleton.service.ConversationMessageService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Gerenciamento de Mensagens em Conversas")
@SecurityRequirement(name = "Bearer Authentication")
public class ConversationMessageController {

    private final ConversationMessageService messageService;
    private final CurrentUserService currentUserService;

    public ConversationMessageController(
        ConversationMessageService messageService,
        CurrentUserService currentUserService
    ) {
        this.messageService = messageService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/conversation/{conversationId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Adicionar Mensagem", description = "Adiciona uma nova mensagem a uma conversa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensagem criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationMessageDto> addMessage(
            @PathVariable Long conversationId,
            @RequestBody ConversationMessageDto dto,
            Authentication authentication) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        ConversationMessageDto created = messageService.addMessage(dto, conversationId, currentUser);
        return ResponseEntity.created(URI.create("/api/messages/" + created.getId())).body(created);
    }

    @PostMapping("/conversation/{conversationId}/exchange")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Troca de Mensagens", description = "Envia uma mensagem do usuário e recebe a resposta do bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Troca processada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationExchangeDto> exchangeMessage(
        @PathVariable Long conversationId,
        @RequestBody ConversationMessageDto dto,
        Authentication authentication
    ) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        ConversationExchangeDto exchange = messageService.processUserMessage(dto, conversationId, currentUser);
        return ResponseEntity.created(URI.create("/api/messages/conversation/" + conversationId + "/exchange")).body(exchange);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Mensagem por ID", description = "Retorna os detalhes de uma mensagem específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagem encontrada"),
            @ApiResponse(responseCode = "404", description = "Mensagem não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationMessageDto> getMessage(@PathVariable Long id, Authentication authentication) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        return messageService.findById(id, currentUser)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/conversation/{conversationId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Mensagens da Conversa", description = "Retorna paginada as mensagens de uma conversa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagens obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<ConversationMessageDto>> listByConversation(
            @PathVariable Long conversationId,
            Pageable pageable,
            Authentication authentication) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        return ResponseEntity.ok(messageService.findByConversationId(conversationId, pageable, currentUser));
    }

    @GetMapping("/conversation/{conversationId}/history")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Histórico da Conversa", description = "Retorna todo o histórico de mensagens de uma conversa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<ConversationMessageDto>> getConversationHistory(@PathVariable Long conversationId, Authentication authentication) {
        UserAccount currentUser = currentUserService.requireCurrentUser(authentication);
        return ResponseEntity.ok(messageService.getConversationHistory(conversationId, currentUser));
    }

    @PatchMapping("/{id}/flag")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Marcar Mensagem com Flag", description = "Marca uma mensagem como importante/flagged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagem marcada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Mensagem não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ConversationMessageDto> flagMessage(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = currentUserService.requireCurrentUserId(authentication);
        return messageService.flagMessage(id, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bot/{botId}/flagged")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Listar Mensagens Flagged", description = "Retorna as mensagens marcadas de um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagens flagged obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<ConversationMessageDto>> listFlaggedMessages(
            @PathVariable Long botId,
            Pageable pageable) {
        return ResponseEntity.ok(messageService.findFlaggedMessages(botId, pageable));
    }

    @GetMapping("/bot/{botId}/stats/avg-response-time")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Tempo Médio de Resposta", description = "Retorna o tempo médio de resposta do bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatística obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Double> getAverageResponseTime(@PathVariable Long botId) {
        return ResponseEntity.ok(messageService.getAverageResponseTime(botId));
    }

    @GetMapping("/bot/{botId}/stats/avg-sentiment")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Sentimento Médio", description = "Retorna o sentimento médio das conversas do bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatística obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Double> getAverageSentimentScore(@PathVariable Long botId) {
        return ResponseEntity.ok(messageService.getAverageSentimentScore(botId));
    }
}
