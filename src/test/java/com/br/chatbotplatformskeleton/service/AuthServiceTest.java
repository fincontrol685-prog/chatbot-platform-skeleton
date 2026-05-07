package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.AuthResponse;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import com.br.chatbotplatformskeleton.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;


     private UserAccount testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authenticationManager, jwtUtil, userRepository, roleRepository, passwordEncoder, emailService, 900000L);

        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USUARIO");

        testUser = new UserAccount();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("encodedPassword");
        testUser.setEnabled(true);
        testUser.setRoles(Set.of(testRole));
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USUARIO")
                .build());
        when(jwtUtil.generateToken("testuser", List.of("USUARIO"))).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.login("testuser", "password");

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertTrue(response.getExpiresIn() > 0);
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testLoginInvalidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login("testuser", "wrongpassword"));
    }

    @Test
    void testLoginWithBlankUsername() {
        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(" ", "password"));
    }

    @Test
    void testLoginWithBlankPassword() {
        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login("testuser", "  "));
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        when(userRepository.existsByUsernameIgnoreCase("newuser")).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("USUARIO")).thenReturn(Optional.of(testRole));

        UserAccount savedUser = new UserAccount();
        savedUser.setId(2L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@example.com");
        savedUser.setPasswordHash("encodedPassword");
        savedUser.setEnabled(true);
        savedUser.setRoles(Set.of(testRole));

        when(userRepository.save(any(UserAccount.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken("newuser", List.of("USUARIO"))).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register("newuser", "newuser@example.com", "password123");

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        verify(userRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    void testRegisterWeakPassword() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.register("newuser", "newuser@example.com", "weak"));
    }

    @Test
    void testRegisterDuplicateUsername() {
        // Arrange
        when(userRepository.existsByUsernameIgnoreCase("testuser")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.register("testuser", "new@example.com", "password123"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterDuplicateEmail() {
        // Arrange
        when(userRepository.existsByUsernameIgnoreCase("newuser")).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.register("newuser", "test@example.com", "password123"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterInvalidEmail() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.register("newuser", "invalid-email", "password123"));
    }

    @Test
    void testRequestPasswordResetUserExists() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        authService.requestPasswordReset("test@example.com");

        // Assert
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(userRepository, times(1)).save(any(UserAccount.class));
        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testRequestPasswordResetUserNotExists() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        authService.requestPasswordReset("nonexistent@example.com");

        // Assert
        verify(userRepository, times(1)).findByEmailIgnoreCase("nonexistent@example.com");
        verify(emailService, never()).sendPasswordResetEmail(any(), any());
    }

    @Test
    void testResetPasswordSuccess() {
        // Arrange
        testUser.setPasswordResetToken("valid-token");
        testUser.setPasswordResetExpiresAt(OffsetDateTime.now().plusHours(1));

        when(userRepository.findByPasswordResetToken("valid-token")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword123")).thenReturn("encodedNewPassword");

        // Act
        authService.resetPassword("valid-token", "newpassword123");

        // Assert
        verify(userRepository, times(1)).findByPasswordResetToken("valid-token");
        verify(userRepository, times(1)).save(any(UserAccount.class));
        assertNull(testUser.getPasswordResetToken());
        assertNull(testUser.getPasswordResetExpiresAt());
    }

    @Test
    void testResetPasswordInvalidToken() {
        // Arrange
        when(userRepository.findByPasswordResetToken("invalid-token")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.resetPassword("invalid-token", "newpassword123"));
    }

    @Test
    void testResetPasswordExpiredToken() {
        // Arrange
        testUser.setPasswordResetToken("expired-token");
        testUser.setPasswordResetExpiresAt(OffsetDateTime.now().minusHours(1));

        when(userRepository.findByPasswordResetToken("expired-token")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.resetPassword("expired-token", "newpassword123"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPasswordWeakPassword() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.resetPassword("valid-token", "weak"));
    }
}
