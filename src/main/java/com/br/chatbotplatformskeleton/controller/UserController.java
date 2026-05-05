package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.RoleOptionDto;
import com.br.chatbotplatformskeleton.dto.UserProfileDto;
import com.br.chatbotplatformskeleton.dto.UserUpsertRequest;
import com.br.chatbotplatformskeleton.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gerenciamento de Usuários")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserProfileService userProfileService;

    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Listar Usuários", description = "Retorna uma lista de usuários com filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<UserProfileDto>> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean enabled
    ) {
        return ResponseEntity.ok(userProfileService.listUsers(search, enabled));
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Listar Papéis (Roles)", description = "Retorna todos os papéis disponíveis no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<RoleOptionDto>> listRoles() {
        return ResponseEntity.ok(userProfileService.listRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Obter Usuário por ID", description = "Retorna os detalhes de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<UserProfileDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getUser(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Criar Novo Usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<UserProfileDto> create(@Valid @RequestBody UserUpsertRequest request) {
        return ResponseEntity.ok(userProfileService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atualizar Usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<UserProfileDto> update(@PathVariable Long id, @Valid @RequestBody UserUpsertRequest request) {
        return ResponseEntity.ok(userProfileService.updateUser(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atualizar Status do Usuário", description = "Ativa ou desativa um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<UserProfileDto> updateStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        return ResponseEntity.ok(userProfileService.updateStatus(id, enabled));
    }
}
