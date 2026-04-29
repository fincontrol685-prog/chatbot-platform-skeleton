package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ConsentLogDto;
import com.br.chatbotplatformskeleton.dto.DataDeletionRequestDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.ComplianceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {

    private final ComplianceService complianceService;
    private final UserRepository userRepository;

    public ComplianceController(ComplianceService complianceService, UserRepository userRepository) {
        this.complianceService = complianceService;
        this.userRepository = userRepository;
    }

    /**
     * Registra consentimento de um usuário
     */
    @PostMapping("/consent")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<ConsentLogDto> grantConsent(
            @RequestParam String consentType,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        ConsentLogDto dto = complianceService.grantConsent(userId, consentType, ipAddress, userAgent);
        return ResponseEntity.ok(dto);
    }

    /**
     * Revoga consentimento (GDPR)
     */
    @DeleteMapping("/consent/{consentType}")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<Void> withdrawConsent(
            @PathVariable String consentType,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        complianceService.withdrawConsent(userId, consentType);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista consentimentos do usuário
     */
    @GetMapping("/consent/my")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<ConsentLogDto>> getMyConsents(Authentication authentication) {
        Long userId = extractUserId(authentication);
        List<ConsentLogDto> consents = complianceService.getUserConsents(userId);
        return ResponseEntity.ok(consents);
    }

    /**
     * Cria requisição de exclusão de dados (GDPR - Direito ao Esquecimento)
     */
    @PostMapping("/data-deletion/request")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<DataDeletionRequestDto> requestDataDeletion(
            @RequestParam String reason,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        DataDeletionRequestDto dto = complianceService.requestDataDeletion(userId, reason, ipAddress);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lista requisições de exclusão do usuário
     */
    @GetMapping("/data-deletion/my")
    @PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
    public ResponseEntity<List<DataDeletionRequestDto>> getMyDeletionRequests(Authentication authentication) {
        Long userId = extractUserId(authentication);
        List<DataDeletionRequestDto> requests = complianceService.getUserDeletionRequests(userId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Aprova requisição de exclusão de dados (ADMIN ONLY)
     */
    @PutMapping("/data-deletion/{requestId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataDeletionRequestDto> approveDeletionRequest(
            @PathVariable Long requestId,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        DataDeletionRequestDto dto = complianceService.approveDeletionRequest(requestId, userId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Rejeita requisição de exclusão de dados (ADMIN ONLY)
     */
    @PutMapping("/data-deletion/{requestId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataDeletionRequestDto> rejectDeletionRequest(
            @PathVariable Long requestId,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        DataDeletionRequestDto dto = complianceService.rejectDeletionRequest(requestId, userId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lista requisições de exclusão pendentes (ADMIN ONLY)
     */
    @GetMapping("/data-deletion/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DataDeletionRequestDto>> getPendingDeletionRequests(Pageable pageable) {
        return ResponseEntity.ok(complianceService.getPendingDeletionRequests(pageable));
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

