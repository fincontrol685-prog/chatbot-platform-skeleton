package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.TwoFactorAuth;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.TwoFactorAuthDto;
import com.br.chatbotplatformskeleton.repository.TwoFactorAuthRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.util.TotpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@Transactional
public class TwoFactorAuthService {

    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public TwoFactorAuthService(TwoFactorAuthRepository twoFactorAuthRepository,
                              UserRepository userRepository,
                              AuditService auditService) {
        this.twoFactorAuthRepository = twoFactorAuthRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    /**
     * Inicia setup de 2FA TOTP para um usuário
     */
    public TwoFactorAuthDto initializeTotpSetup(Long userId) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        String secretKey = TotpUtil.generateSecretKey();
        String qrCodeUrl = TotpUtil.getQrCodeUrl(secretKey, userOpt.get().getUsername(), "ChatbotPlatform");

        Optional<TwoFactorAuth> existing = twoFactorAuthRepository.findByUserId(userId);
        TwoFactorAuth tfa;

        if (existing.isPresent()) {
            tfa = existing.get();
            tfa.setSecretKey(secretKey);
            tfa.setIsVerified(false);
            tfa.setMethod("TOTP");
        } else {
            tfa = new TwoFactorAuth();
            tfa.setUser(userOpt.get());
            tfa.setSecretKey(secretKey);
            tfa.setMethod("TOTP");
            tfa.setIsVerified(false);
            tfa.setCreatedAt(OffsetDateTime.now());
        }

        twoFactorAuthRepository.save(tfa);
        auditService.log(userId, "INIT_SETUP", "2FA", tfa.getId(), null, "TOTP setup initiated");

        return toDto(tfa, qrCodeUrl);
    }

    /**
     * Verifica o código TOTP e ativa 2FA se válido
     */
    public TwoFactorAuthDto verifyAndActivateTwoFactorAuth(Long userId, String totpCode) {
        Optional<TwoFactorAuth> tfaOpt = twoFactorAuthRepository.findByUserId(userId);
        if (tfaOpt.isEmpty()) {
            throw new IllegalArgumentException("2FA not initialized for user: " + userId);
        }

        TwoFactorAuth tfa = tfaOpt.get();
        if (tfa.getSecretKey() == null) {
            throw new IllegalStateException("Secret key not found");
        }

        boolean isValid = TotpUtil.validate(tfa.getSecretKey(), totpCode);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid TOTP code");
        }

        // Generate backup codes
        String[] backupCodes = TotpUtil.generateBackupCodes(10);
        tfa.setBackupCodes(String.join(",", backupCodes));
        tfa.setIsEnabled(true);
        tfa.setIsVerified(true);
        tfa.setLastVerifiedAt(OffsetDateTime.now());

        TwoFactorAuth saved = twoFactorAuthRepository.save(tfa);
        auditService.log(userId, "ACTIVATE", "2FA", saved.getId(), null, "2FA activated successfully");

        TwoFactorAuthDto dto = toDto(saved, null);
        dto.setIsVerified(true);
        return dto;
    }

    /**
     * Valida um código TOTP ou backup code
     */
    public boolean validateTwoFactorCode(Long userId, String code) {
        Optional<TwoFactorAuth> tfaOpt = twoFactorAuthRepository.findByUserIdAndIsEnabledTrue(userId);
        if (tfaOpt.isEmpty()) {
            return false;  // 2FA não está habilitado
        }

        TwoFactorAuth tfa = tfaOpt.get();

        // Validar TOTP
        if (TotpUtil.validate(tfa.getSecretKey(), code)) {
            tfa.setLastVerifiedAt(OffsetDateTime.now());
            twoFactorAuthRepository.save(tfa);
            return true;
        }

        // Validar backup codes
        if (tfa.getBackupCodes() != null) {
            String[] codes = tfa.getBackupCodes().split(",");
            String[] usedCodes = tfa.getBackupCodesUsed() != null ? tfa.getBackupCodesUsed().split(",") : new String[0];

            for (String backupCode : codes) {
                if (backupCode.equals(code)) {
                    // Verificar se já foi usado
                    for (String used : usedCodes) {
                        if (used.equals(code)) {
                            return false;  // Código de backup já foi usado
                        }
                    }

                    // Marcar como usado
                    String updatedUsedCodes = tfa.getBackupCodesUsed() != null ?
                        tfa.getBackupCodesUsed() + "," + code : code;
                    tfa.setBackupCodesUsed(updatedUsedCodes);
                    tfa.setLastVerifiedAt(OffsetDateTime.now());
                    twoFactorAuthRepository.save(tfa);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Desativa 2FA para um usuário
     */
    public void disableTwoFactorAuth(Long userId, Long requesterId) {
        Optional<TwoFactorAuth> tfaOpt = twoFactorAuthRepository.findByUserId(userId);
        if (tfaOpt.isPresent()) {
            TwoFactorAuth tfa = tfaOpt.get();
            tfa.setIsEnabled(false);
            twoFactorAuthRepository.save(tfa);
            auditService.log(requesterId, "DISABLE", "2FA", tfa.getId(), null, "2FA disabled");
        }
    }

    public Optional<TwoFactorAuthDto> getTwoFactorAuthStatus(Long userId) {
        return twoFactorAuthRepository.findByUserId(userId).map(this::toDto);
    }

    private TwoFactorAuthDto toDto(TwoFactorAuth tfa) {
        return toDto(tfa, null);
    }

    private TwoFactorAuthDto toDto(TwoFactorAuth tfa, String qrCode) {
        TwoFactorAuthDto dto = new TwoFactorAuthDto();
        dto.setId(tfa.getId());
        dto.setUserId(tfa.getUser().getId());
        dto.setIsEnabled(tfa.getIsEnabled());
        dto.setMethod(tfa.getMethod());
        dto.setPhoneNumber(tfa.getPhoneNumber());
        dto.setIsVerified(tfa.getIsVerified());
        dto.setCreatedAt(tfa.getCreatedAt());
        dto.setLastVerifiedAt(tfa.getLastVerifiedAt());
        dto.setQrCode(qrCode);
        return dto;
    }
}

