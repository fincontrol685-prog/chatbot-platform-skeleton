package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ConsentLogDto;
import com.br.chatbotplatformskeleton.dto.DataDeletionRequestDto;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.service.ComplianceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Compliance", description = "Gerenciamento de Conformidade e GDPR")
@SecurityRequirement(name = "Bearer Authentication")
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
    @Operation(summary = "Conceder Consentimento", description = "Registra o consentimento do usuário para uma atividade específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consentimento registrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Tipo de consentimento inválido")
    })
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
    @Operation(summary = "Revogar Consentimento", description = "Revoga o consentimento do usuário (GDPR - Direito ao Arrependimento)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consentimento revogado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Consentimento não encontrado")
    })
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
    @Operation(summary = "Meus Consentimentos", description = "Retorna todos os consentimentos do usuário atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consentimentos obtidos com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
    @Operation(summary = "Solicitar Exclusão de Dados", description = "Cria uma requisição para exclusão de dados do usuário (GDPR - Direito ao Esquecimento)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição criada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Motivo inválido")
    })
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
    @Operation(summary = "Minhas Requisições de Exclusão", description = "Retorna todas as requisições de exclusão de dados do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisições obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
    @Operation(summary = "Aprovar Exclusão de Dados", description = "Aprova uma requisição de exclusão de dados (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição aprovada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Requisição não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
    @Operation(summary = "Rejeitar Exclusão de Dados", description = "Rejeita uma requisição de exclusão de dados (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição rejeitada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Requisição não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
    @Operation(summary = "Requisições Pendentes", description = "Lista todas as requisições de exclusão pendentes (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisições obtidas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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

