package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.TwoFactorAuthDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.TwoFactorAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Two Factor Authentication", description = "Autenticação de Dois Fatores (2FA)")
@SecurityRequirement(name = "Bearer Authentication")
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
    @Operation(summary = "Inicializar Autenticação de Dois Fatores", description = "Gera um código QR para configurar 2FA via TOTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "2FA inicializado, retorna código QR"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Usuário não identificado")
    })
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
    @Operation(summary = "Verificar e Ativar 2FA", description = "Verifica o código TOTP e ativa a autenticação de dois fatores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "2FA verificado e ativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código inválido ou usuário não identificado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
    @Operation(summary = "Validar Código 2FA", description = "Valida um código TOTP para login com 2FA ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código validado, retorna resultado"),
            @ApiResponse(responseCode = "400", description = "Usuário ou código inválido")
    })
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
    @Operation(summary = "Obter Status do 2FA", description = "Retorna o status da autenticação de dois fatores do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status obtido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
    @Operation(summary = "Desativar 2FA", description = "Desativa a autenticação de dois fatores do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "2FA desativado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Usuário não identificado")
    })
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

