package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.TwoFactorAuthDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.TwoFactorAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TwoFactorAuthController.class);

    private final TwoFactorAuthService twoFactorAuthService;
    private final UserRepository userRepository;

    public TwoFactorAuthController(TwoFactorAuthService twoFactorAuthService, UserRepository userRepository) {
        this.twoFactorAuthService = twoFactorAuthService;
        this.userRepository = userRepository;
    }

    @PostMapping("/init")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<TwoFactorAuthDto> initializeTwoFactorAuth(Authentication authentication) {
        Long userId = extractUserId(authentication);
        if (userId == null) {
            throw new IllegalArgumentException("Unable to extract user ID from authentication");
        }
        TwoFactorAuthDto dto = twoFactorAuthService.initializeTotpSetup(userId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/verify")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Map<String, Object>> verifyAndActivate(
            @RequestParam String code,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        if (userId == null) {
            throw new IllegalArgumentException("Unable to extract user ID from authentication");
        }
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
        if (userId == null) {
            logger.warn("Could not extract user ID from authentication, returning empty 2FA status");
            return ResponseEntity.ok(new TwoFactorAuthDto());
        }
        return twoFactorAuthService.getTwoFactorAuthStatus(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new TwoFactorAuthDto()));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Void> disableTwoFactorAuth(Authentication authentication) {
        Long userId = extractUserId(authentication);
        if (userId == null) {
            throw new IllegalArgumentException("Unable to extract user ID from authentication");
        }
        twoFactorAuthService.disableTwoFactorAuth(userId, userId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null) {
            logger.warn("Authentication is null while extracting user ID");
            return null;
        }

        Object principal = authentication.getPrincipal();
        String username = null;

        // Handle case where principal is a String (username from JWT)
        if (principal instanceof String) {
            username = (String) principal;
        }
        // Handle case where principal is a UserDetails object
        else if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        // Unknown principal type
        else {
            logger.warn("Principal is not a String or UserDetails: {}",
                principal.getClass().getName());
            return null;
        }

        if (username == null || username.trim().isEmpty()) {
            logger.warn("Username is null or empty");
            return null;
        }

        var user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username);
        if (user.isEmpty()) {
            logger.warn("User not found in repository for username: {}", username);
            return null;
        }

        return user.get().getId();
    }
}

