package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.AuthRequest;
import com.br.chatbotplatformskeleton.dto.AuthResponse;
import com.br.chatbotplatformskeleton.dto.RegisterRequest;
import com.br.chatbotplatformskeleton.dto.ForgotPasswordRequest;
import com.br.chatbotplatformskeleton.dto.ResetPasswordRequest;
import com.br.chatbotplatformskeleton.service.AuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody(required = false) Object body) {
        // Simplified: in MVP we can re-issue token by validating refresh token from cookie or body.
        return ResponseEntity.badRequest().build();
    }
}
