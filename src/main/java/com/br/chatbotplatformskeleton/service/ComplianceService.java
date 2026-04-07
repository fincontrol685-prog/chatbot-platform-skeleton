package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.ConsentLog;
import com.br.chatbotplatformskeleton.domain.DataDeletionRequest;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.ConsentLogDto;
import com.br.chatbotplatformskeleton.dto.DataDeletionRequestDto;
import com.br.chatbotplatformskeleton.repository.ConsentLogRepository;
import com.br.chatbotplatformskeleton.repository.DataDeletionRequestRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComplianceService {

    private final ConsentLogRepository consentLogRepository;
    private final DataDeletionRequestRepository dataDeletionRequestRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public ComplianceService(ConsentLogRepository consentLogRepository,
                            DataDeletionRequestRepository dataDeletionRequestRepository,
                            UserRepository userRepository,
                            AuditService auditService) {
        this.consentLogRepository = consentLogRepository;
        this.dataDeletionRequestRepository = dataDeletionRequestRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    /**
     * Registra consentimento de um usuário
     */
    public ConsentLogDto grantConsent(Long userId, String consentType, String ipAddress, String userAgent) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        ConsentLog consent = new ConsentLog();
        consent.setUser(userOpt.get());
        consent.setConsentType(consentType);
        consent.setIsGranted(true);
        consent.setIpAddress(ipAddress);
        consent.setUserAgent(userAgent);
        consent.setConsentVersion("1.0");
        consent.setCreatedAt(OffsetDateTime.now());

        ConsentLog saved = consentLogRepository.save(consent);
        auditService.log(userId, "GRANTED", "CONSENT", saved.getId(), null, consentType);
        return toConsentDto(saved);
    }

    /**
     * Revoga consentimento de um usuário (GDPR - Direito de Retirar Consentimento)
     */
    public void withdrawConsent(Long userId, String consentType) {
        List<ConsentLog> consents = consentLogRepository.findActiveConsentsByUserAndType(userId, consentType);
        for (ConsentLog consent : consents) {
            consent.setWithdrawnAt(OffsetDateTime.now());
            consentLogRepository.save(consent);
        }
        auditService.log(userId, "WITHDRAWN", "CONSENT", userId, null, consentType);
    }

    /**
     * Lista todos os consentimentos de um usuário
     */
    public List<ConsentLogDto> getUserConsents(Long userId) {
        return consentLogRepository.findAllByUserId(userId)
            .stream()
            .map(this::toConsentDto)
            .collect(Collectors.toList());
    }

    /**
     * Cria uma requisição de exclusão de dados (GDPR - Direito ao Esquecimento)
     */
    public DataDeletionRequestDto requestDataDeletion(Long userId, String reason, String ipAddress) {
        Optional<UserAccount> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Verificar se já existe requisição pendente
        Optional<DataDeletionRequest> existingRequest = dataDeletionRequestRepository.findByUserIdAndStatus(userId, "PENDING");
        if (existingRequest.isPresent()) {
            throw new IllegalStateException("User already has a pending deletion request");
        }

        DataDeletionRequest request = new DataDeletionRequest();
        request.setUser(userOpt.get());
        request.setStatus("PENDING");
        request.setReason(reason);
        request.setIpAddress(ipAddress);
        request.setRequestedAt(OffsetDateTime.now());

        DataDeletionRequest saved = dataDeletionRequestRepository.save(request);
        auditService.log(userId, "REQUEST", "DATA_DELETION", saved.getId(), null, reason);
        return toDataDeletionDto(saved);
    }

    /**
     * Aprova uma requisição de exclusão de dados
     */
    public DataDeletionRequestDto approveDeletionRequest(Long requestId, Long approvedBy) {
        Optional<DataDeletionRequest> requestOpt = dataDeletionRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new IllegalArgumentException("Deletion request not found: " + requestId);
        }

        DataDeletionRequest request = requestOpt.get();
        request.setStatus("APPROVED");
        request.setProcessedAt(OffsetDateTime.now());
        request.setProcessedBy(approvedBy);

        DataDeletionRequest saved = dataDeletionRequestRepository.save(request);
        auditService.log(approvedBy, "APPROVED", "DATA_DELETION", saved.getId(), null, "approved");
        return toDataDeletionDto(saved);
    }

    /**
     * Rejeita uma requisição de exclusão de dados
     */
    public DataDeletionRequestDto rejectDeletionRequest(Long requestId, Long rejectedBy) {
        Optional<DataDeletionRequest> requestOpt = dataDeletionRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new IllegalArgumentException("Deletion request not found: " + requestId);
        }

        DataDeletionRequest request = requestOpt.get();
        request.setStatus("REJECTED");
        request.setProcessedAt(OffsetDateTime.now());
        request.setProcessedBy(rejectedBy);

        DataDeletionRequest saved = dataDeletionRequestRepository.save(request);
        auditService.log(rejectedBy, "REJECTED", "DATA_DELETION", saved.getId(), null, "rejected");
        return toDataDeletionDto(saved);
    }

    /**
     * Lista requisições de exclusão pendentes
     */
    public Page<DataDeletionRequestDto> getPendingDeletionRequests(Pageable pageable) {
        return dataDeletionRequestRepository.findByStatus("PENDING", pageable)
            .map(this::toDataDeletionDto);
    }

    /**
     * Obtém histórico de requisições de exclusão de um usuário
     */
    public List<DataDeletionRequestDto> getUserDeletionRequests(Long userId) {
        return dataDeletionRequestRepository.findByUserId(userId)
            .stream()
            .map(this::toDataDeletionDto)
            .collect(Collectors.toList());
    }

    private ConsentLogDto toConsentDto(ConsentLog consent) {
        ConsentLogDto dto = new ConsentLogDto();
        dto.setId(consent.getId());
        dto.setUserId(consent.getUser().getId());
        dto.setConsentType(consent.getConsentType());
        dto.setIsGranted(consent.getIsGranted());
        dto.setConsentVersion(consent.getConsentVersion());
        dto.setCreatedAt(consent.getCreatedAt());
        dto.setWithdrawnAt(consent.getWithdrawnAt());
        return dto;
    }

    private DataDeletionRequestDto toDataDeletionDto(DataDeletionRequest request) {
        DataDeletionRequestDto dto = new DataDeletionRequestDto();
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setStatus(request.getStatus());
        dto.setReason(request.getReason());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setProcessedAt(request.getProcessedAt());
        return dto;
    }
}

