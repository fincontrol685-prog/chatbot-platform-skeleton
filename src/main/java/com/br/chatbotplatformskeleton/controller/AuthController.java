package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.AuthRequest;
import com.br.chatbotplatformskeleton.dto.AuthResponse;
import com.br.chatbotplatformskeleton.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody(required = false) Object body) {
        // Simplified: in MVP we can re-issue token by validating refresh token from cookie or body.
        return ResponseEntity.badRequest().build();
    }
}
