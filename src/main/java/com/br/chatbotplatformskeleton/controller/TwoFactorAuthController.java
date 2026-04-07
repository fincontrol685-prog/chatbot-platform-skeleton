package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.TwoFactorAuthDto;
import com.br.chatbotplatformskeleton.service.TwoFactorAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/security/2fa")
public class TwoFactorAuthController {

    private final TwoFactorAuthService twoFactorAuthService;

    public TwoFactorAuthController(TwoFactorAuthService twoFactorAuthService) {
        this.twoFactorAuthService = twoFactorAuthService;
    }

    @PostMapping("/init")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<TwoFactorAuthDto> initializeTwoFactorAuth(Authentication authentication) {
        Long userId = extractUserId(authentication);
        TwoFactorAuthDto dto = twoFactorAuthService.initializeTotpSetup(userId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/verify")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Map<String, Object>> verifyAndActivate(
            @RequestParam String code,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        TwoFactorAuthDto dto = twoFactorAuthService.verifyAndActivateTwoFactorAuth(userId, code);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "2FA activated successfully");
        response.put("twoFactorAuth", dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateCode(
            @RequestParam Long userId,
            @RequestParam String code) {
        boolean isValid = twoFactorAuthService.validateTwoFactorCode(userId, code);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<TwoFactorAuthDto> getTwoFactorStatus(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return twoFactorAuthService.getTwoFactorAuthStatus(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Void> disableTwoFactorAuth(Authentication authentication) {
        Long userId = extractUserId(authentication);
        twoFactorAuthService.disableTwoFactorAuth(userId, userId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // In a real application, you would look up the user ID by username
            return 1L;  // Placeholder
        }
        return null;
    }
}

