package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long requireCurrentUserId(Authentication authentication) {
        return requireCurrentUser(authentication).getId();
    }

    public UserAccount requireCurrentUser(Authentication authentication) {
        String principal = extractPrincipal(authentication);
        return userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(principal, principal)
            .orElseThrow(() -> new IllegalArgumentException("Usuario autenticado nao encontrado"));
    }

    public boolean isPrivileged(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> "ROLE_ADMIN".equals(authority) || "ROLE_GESTOR".equals(authority));
    }

    public boolean isPrivileged(UserAccount userAccount) {
        if (userAccount == null || userAccount.getRoles() == null) {
            return false;
        }

        return userAccount.getRoles().stream()
            .map(role -> role.getName())
            .anyMatch(roleName -> "ADMIN".equalsIgnoreCase(roleName) || "GESTOR".equalsIgnoreCase(roleName));
    }

    private String extractPrincipal(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Usuario autenticado nao encontrado");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof String value && !value.isBlank()) {
            return value;
        }

        throw new IllegalArgumentException("Usuario autenticado nao encontrado");
    }
}
