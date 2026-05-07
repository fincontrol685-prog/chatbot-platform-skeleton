package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.application.bot.usecase.BotManagementUseCase;
import com.br.chatbotplatformskeleton.application.bot.usecase.BotView;
import com.br.chatbotplatformskeleton.application.bot.usecase.UpsertBotCommand;
import com.br.chatbotplatformskeleton.dto.BotDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bots")
@Tag(name = "Bots", description = "Gerenciamento de Bots de Conversa")
@SecurityRequirement(name = "Bearer Authentication")
public class BotController {

    private final BotManagementUseCase botManagementUseCase;

    public BotController(BotManagementUseCase botManagementUseCase) {
        this.botManagementUseCase = botManagementUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Todos os Bots", description = "Retorna uma lista de todos os bots cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bots obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<BotDto>> list() {
        return ResponseEntity.ok(botManagementUseCase.listAll().stream().map(this::toDto).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Criar Novo Bot", description = "Cria um novo bot de conversa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bot criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotDto> create(@Valid @RequestBody BotDto dto) {
        BotDto created = toDto(botManagementUseCase.create(toCommand(dto)));
        return ResponseEntity.created(URI.create("/api/bots/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Bot por ID", description = "Retorna os detalhes de um bot específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bot encontrado"),
            @ApiResponse(responseCode = "404", description = "Bot não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotDto> get(@PathVariable Long id) {
        return botManagementUseCase.findById(id)
            .map(this::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Atualizar Bot", description = "Atualiza os dados de um bot existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bot atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bot não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotDto> update(@PathVariable Long id, @Valid @RequestBody BotDto dto) {
        return botManagementUseCase.update(id, toCommand(dto))
            .map(this::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Ativar/Desativar Bot", description = "Alterna o status de ativação de um bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do bot alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bot não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotDto> activate(@PathVariable Long id, @RequestParam(value = "active", defaultValue = "true") boolean active) {
        return botManagementUseCase.activate(id, active)
            .map(this::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private UpsertBotCommand toCommand(BotDto dto) {
        return new UpsertBotCommand(dto.getName(), dto.getKey(), dto.getEnabled(), dto.getConfig());
    }

    private BotDto toDto(BotView bot) {
        BotDto dto = new BotDto();
        dto.setId(bot.id());
        dto.setName(bot.name());
        dto.setKey(bot.key());
        dto.setEnabled(bot.enabled());
        dto.setConfig(bot.config());
        return dto;
    }
}
