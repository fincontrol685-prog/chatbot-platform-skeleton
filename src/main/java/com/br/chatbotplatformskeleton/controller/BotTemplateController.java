package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.BotTemplateDto;
import com.br.chatbotplatformskeleton.service.BotTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/templates")
public class BotTemplateController {

    private final BotTemplateService templateService;

    public BotTemplateController(BotTemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<BotTemplateDto> createTemplate(
            @RequestBody BotTemplateDto dto,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        BotTemplateDto created = templateService.create(dto, userId);
        return ResponseEntity.created(URI.create("/api/templates/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<BotTemplateDto> getTemplate(@PathVariable Long id) {
        return templateService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<BotTemplateDto>> listPublicTemplates(Pageable pageable) {
        return ResponseEntity.ok(templateService.listPublic(pageable));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<BotTemplateDto>> listByCategory(
            @PathVariable String category,
            Pageable pageable) {
        return ResponseEntity.ok(templateService.listByCategory(category, pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<BotTemplateDto>> listByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(templateService.listByUser(userId, pageable));
    }

    @GetMapping("/most-used")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<BotTemplateDto>> listMostUsed(Pageable pageable) {
        return ResponseEntity.ok(templateService.listMostUsed(pageable));
    }

    @GetMapping("/top-rated")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Page<BotTemplateDto>> listTopRated(Pageable pageable) {
        return ResponseEntity.ok(templateService.listTopRated(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
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

