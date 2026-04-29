package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.TeamDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.TeamService;
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
public class TeamController {

    private final TeamService teamService;
    private final UserRepository userRepository;

    public TeamController(TeamService teamService, UserRepository userRepository) {
        this.teamService = teamService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<TeamDto> create(@RequestBody TeamDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        TeamDto created = teamService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/teams/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<TeamDto> getById(@PathVariable Long id) {
        return teamService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<TeamDto>> list(Pageable pageable) {
        return ResponseEntity.ok(teamService.listActive(pageable));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<TeamDto>> listByDepartment(@PathVariable Long departmentId, Pageable pageable) {
        return ResponseEntity.ok(teamService.listByDepartment(departmentId, pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<TeamDto>> search(@RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(teamService.search(q, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<TeamDto> update(@PathVariable Long id, @RequestBody TeamDto dto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        TeamDto updated = teamService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Void> addMember(@PathVariable Long id, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        teamService.addMember(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        teamService.removeMember(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/lead/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Void> assignTeamLead(@PathVariable Long id, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        teamService.assignTeamLead(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

