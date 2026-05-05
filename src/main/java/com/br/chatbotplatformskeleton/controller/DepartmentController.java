package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.DepartmentDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.DepartmentService;
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
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Departments", description = "Gerenciamento de Departamentos")
@SecurityRequirement(name = "Bearer Authentication")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserRepository userRepository;

    public DepartmentController(DepartmentService departmentService, UserRepository userRepository) {
        this.departmentService = departmentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Criar Departamento", description = "Cria um novo departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Departamento criado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        DepartmentDto created = departmentService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/departments/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Buscar Departamento por ID", description = "Retorna os detalhes de um departamento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id) {
        return departmentService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Departamentos Ativos", description = "Retorna uma página de departamentos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de departamentos obtida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<DepartmentDto>> list(Pageable pageable) {
        return ResponseEntity.ok(departmentService.listActive(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Buscar Departamentos", description = "Busca departamentos por nome ou critério")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados da busca retornados"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<DepartmentDto>> search(@RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(departmentService.search(q, pageable));
    }

    @GetMapping("/root")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Departamentos Raiz", description = "Retorna todos os departamentos raiz (sem parent)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de departamentos raiz obtida"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<DepartmentDto>> getRootDepartments() {
        return ResponseEntity.ok(departmentService.getRootDepartments());
    }

    @GetMapping("/{id}/subdepartments")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    @Operation(summary = "Listar Subdepartamentos", description = "Retorna todos os subdepartamentos de um departamento pai")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de subdepartamentos obtida"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<DepartmentDto>> getSubDepartments(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getSubDepartments(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    @Operation(summary = "Atualizar Departamento", description = "Atualiza os dados de um departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody DepartmentDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        DepartmentDto updated = departmentService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar Departamento", description = "Deleta um departamento (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Departamento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        departmentService.delete(id, userId);
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

