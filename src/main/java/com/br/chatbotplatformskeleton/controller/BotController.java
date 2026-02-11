package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.BotDto;
import com.br.chatbotplatformskeleton.service.BotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotService botService;

    public BotController(BotService botService) {
        this.botService = botService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<BotDto>> list() {
        return ResponseEntity.ok(botService.listAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<BotDto> create(@RequestBody BotDto dto) {
        BotDto created = botService.create(dto);
        return ResponseEntity.created(URI.create("/api/bots/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<BotDto> get(@PathVariable Long id) {
        return botService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<BotDto> activate(@PathVariable Long id, @RequestParam(value = "active", defaultValue = "true") boolean active) {
        return botService.activate(id, active).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
