package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.TeamDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.TeamService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Gerenciamento de Equipes")
@SecurityRequirement(name = "Bearer Authentication")
public class TeamController {

    private final TeamService teamService;
    private final UserRepository userRepository;

    public TeamController(TeamService teamService, UserRepository userRepository) {
        this.teamService = teamService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Criar Equipe", description = "Cria uma nova equipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipe criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<TeamDto> create(@RequestBody TeamDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        TeamDto created = teamService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/teams/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Obter Equipe por ID", description = "Retorna os detalhes de uma equipe específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipe encontrada"),
            @ApiResponse(responseCode = "404", description = "Equipe não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<TeamDto> getById(@PathVariable Long id) {
        return teamService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Equipes Ativas", description = "Retorna uma página de equipes ativas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipes obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<TeamDto>> list(Pageable pageable) {
        return ResponseEntity.ok(teamService.listActive(pageable));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Equipes por Departamento", description = "Retorna todas as equipes de um departamento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipes obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<TeamDto>> listByDepartment(@PathVariable Long departmentId, Pageable pageable) {
        return ResponseEntity.ok(teamService.listByDepartment(departmentId, pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Buscar Equipes", description = "Busca equipes por nome ou critério")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados da busca retornados"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<TeamDto>> search(@RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(teamService.search(q, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atualizar Equipe", description = "Atualiza os dados de uma equipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipe atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipe não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TeamDto> update(@PathVariable Long id, @RequestBody TeamDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        TeamDto updated = teamService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Adicionar Membro à Equipe", description = "Adiciona um usuário como membro de uma equipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membro adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipe ou usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> addMember(@PathVariable Long id, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        teamService.addMember(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Remover Membro da Equipe", description = "Remove um membro de uma equipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipe ou membro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        teamService.removeMember(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/lead/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atribuir Líder de Equipe", description = "Define um usuário como líder de uma equipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Líder atribuído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipe ou usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> assignTeamLead(@PathVariable Long id, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        teamService.assignTeamLead(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar Equipe", description = "Deleta uma equipe (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipe deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipe não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        teamService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
                .map(user -> user.getId())
                .orElse(null);
        }
        return null;
    }
}

