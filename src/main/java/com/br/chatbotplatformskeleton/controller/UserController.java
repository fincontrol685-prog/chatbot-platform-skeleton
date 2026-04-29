package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.RoleOptionDto;
import com.br.chatbotplatformskeleton.dto.UserProfileDto;
import com.br.chatbotplatformskeleton.dto.UserUpsertRequest;
import com.br.chatbotplatformskeleton.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserProfileService userProfileService;

    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<UserProfileDto>> list(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean enabled
    ) {
        return ResponseEntity.ok(userProfileService.listUsers(search, enabled));
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<RoleOptionDto>> listRoles() {
        return ResponseEntity.ok(userProfileService.listRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<UserProfileDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getUser(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<UserProfileDto> create(@Valid @RequestBody UserUpsertRequest request) {
        return ResponseEntity.ok(userProfileService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<UserProfileDto> update(@PathVariable Long id, @Valid @RequestBody UserUpsertRequest request) {
        return ResponseEntity.ok(userProfileService.updateUser(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<UserProfileDto> updateStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        return ResponseEntity.ok(userProfileService.updateStatus(id, enabled));
    }
}
