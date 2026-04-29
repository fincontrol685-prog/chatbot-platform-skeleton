package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.DepartmentDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.DepartmentService;
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
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserRepository userRepository;

    public DepartmentController(DepartmentService departmentService, UserRepository userRepository) {
        this.departmentService = departmentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        DepartmentDto created = departmentService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/departments/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id) {
        return departmentService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<DepartmentDto>> list(Pageable pageable) {
        return ResponseEntity.ok(departmentService.listActive(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<DepartmentDto>> search(@RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(departmentService.search(q, pageable));
    }

    @GetMapping("/root")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<DepartmentDto>> getRootDepartments() {
        return ResponseEntity.ok(departmentService.getRootDepartments());
    }

    @GetMapping("/{id}/subdepartments")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<DepartmentDto>> getSubDepartments(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getSubDepartments(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody DepartmentDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        DepartmentDto updated = departmentService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

