package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.AuthResponse;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final long tokenExpirationSeconds;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService,
                       @Value("${security.jwt.expiration-ms:900000}") long tokenExpirationMs) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenExpirationSeconds = tokenExpirationMs / 1000L;
    }

    public AuthResponse login(String username, String password) {
        String principal = normalizeCredential(username, "Usuario ou email");
        String rawPassword = normalizeCredential(password, "Senha");

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(principal, rawPassword)
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).map(a -> a.replace("ROLE_", "")).collect(Collectors.toList());
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return new AuthResponse(token, tokenExpirationSeconds);
    }

    public AuthResponse register(String username, String email, String password) {
        String normalizedUsername = normalizeRequired(username, "Usuario");
        String normalizedEmail = normalizeEmail(email);
        String normalizedPassword = normalizeRequired(password, "Senha");

        if (normalizedPassword.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no minimo 8 caracteres");
        }

        if (userRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new IllegalArgumentException("Usuario ja cadastrado");
        }

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }

        UserAccount user = new UserAccount();
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(normalizedPassword));
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("USUARIO")
            .orElseGet(() -> {
                Role role = new Role();
                role.setName("USUARIO");
                role.setDescription("Usuário padrão");
                return roleRepository.save(role);
            });
        user.setRoles(Set.of(userRole));

        UserAccount saved = userRepository.save(user);

        List<String> roles = saved.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());
        String token = jwtUtil.generateToken(saved.getUsername(), roles);
        return new AuthResponse(token, tokenExpirationSeconds);
    }

    public void requestPasswordReset(String email) {
        String normalizedEmail = normalizeEmail(email);
        userRepository.findByEmailIgnoreCase(normalizedEmail).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            user.setPasswordResetExpiresAt(OffsetDateTime.now().plusHours(1));
            userRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });
    }

    public void resetPassword(String token, String newPassword) {
        String normalizedToken = normalizeRequired(token, "Token");
        String normalizedPassword = normalizeRequired(newPassword, "Nova senha");

        if (normalizedPassword.length() < 8) {
            throw new IllegalArgumentException("Nova senha deve ter no minimo 8 caracteres");
        }

        UserAccount user = userRepository.findByPasswordResetToken(normalizedToken)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado"));

        if (user.getPasswordResetExpiresAt() == null || user.getPasswordResetExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Token inválido ou expirado");
        }

        user.setPasswordHash(passwordEncoder.encode(normalizedPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        userRepository.save(user);
    }

    private String normalizeCredential(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadCredentialsException(fieldName + " invalido");
        }
        return value.trim();
    }

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " invalido");
        }
        return value.trim();
    }

    private String normalizeEmail(String email) {
        String normalizedEmail = normalizeRequired(email, "Email").toLowerCase();
        if (!normalizedEmail.contains("@")) {
            throw new IllegalArgumentException("Email invalido");
        }
        return normalizedEmail;
    }
}
