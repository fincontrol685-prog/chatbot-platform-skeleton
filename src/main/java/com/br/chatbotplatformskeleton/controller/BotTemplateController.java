package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.BotTemplateDto;
import com.br.chatbotplatformskeleton.service.BotTemplateService;
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

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Templates", description = "Gerenciamento de Templates de Bots")
@SecurityRequirement(name = "Bearer Authentication")
public class BotTemplateController {

    private final BotTemplateService templateService;

    public BotTemplateController(BotTemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Criar Template de Bot", description = "Cria um novo template de bot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotTemplateDto> createTemplate(
            @RequestBody BotTemplateDto dto,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        BotTemplateDto created = templateService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/templates/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Template por ID", description = "Retorna os detalhes de um template específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template encontrado"),
            @ApiResponse(responseCode = "404", description = "Template não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotTemplateDto> getTemplate(@PathVariable Long id) {
        return templateService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Templates Públicos", description = "Retorna uma página de templates públicos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de templates obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<BotTemplateDto>> listPublicTemplates(Pageable pageable) {
        return ResponseEntity.ok(templateService.listPublic(pageable));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Templates por Categoria", description = "Retorna templates de uma categoria específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de templates obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<BotTemplateDto>> listByCategory(
            @PathVariable String category,
            Pageable pageable) {
        return ResponseEntity.ok(templateService.listByCategory(category, pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Templates por Usuário", description = "Retorna todos os templates criados por um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de templates obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<BotTemplateDto>> listByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(templateService.listByUser(userId, pageable));
    }

    @GetMapping("/most-used")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Templates Mais Usados", description = "Retorna os templates mais utilizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de templates obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<BotTemplateDto>> listMostUsed(Pageable pageable) {
        return ResponseEntity.ok(templateService.listMostUsed(pageable));
    }

    @GetMapping("/top-rated")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Templates Melhor Avaliados", description = "Retorna os templates com melhor avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de templates obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<BotTemplateDto>> listTopRated(Pageable pageable) {
        return ResponseEntity.ok(templateService.listTopRated(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atualizar Template", description = "Atualiza os dados de um template existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Template não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotTemplateDto> updateTemplate(
            @PathVariable Long id,
            @RequestBody BotTemplateDto dto,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        return templateService.updateTemplate(id, dto, userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/rate")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Avaliar Template", description = "Adiciona uma avaliação a um template (0 a 5 estrelas)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template avaliado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Template não encontrado"),
            @ApiResponse(responseCode = "400", description = "Avaliação inválida (deve estar entre 0 e 5)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<BotTemplateDto> rateTemplate(
            @PathVariable Long id,
            @RequestParam Double rating) {
        if (rating < 0 || rating > 5) {
            return ResponseEntity.badRequest().build();
        }
        return templateService.rateTemplate(id, rating)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private Long extractUserId(Authentication authentication) {
        return 1L; // Implement based on your user retrieval logic
    }
}

