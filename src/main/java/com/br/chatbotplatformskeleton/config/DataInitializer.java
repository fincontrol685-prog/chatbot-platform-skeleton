package com.br.chatbotplatformskeleton.config;

import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initializes default data (users, roles) on application startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = ensureRole("ADMIN", "Administrador do sistema");
        ensureRole("GESTOR", "Gestor");
        Role userRole = ensureRole("USUARIO", "Usuario padrao");

        ensureUser("admin", "admin@chatbot.local", "admin123", adminRole);
        ensureUser("user", "user@chatbot.local", "user12345", userRole);

        log.info("Application data initialized successfully");
    }

    private Role ensureRole(String name, String description) {
        return roleRepository.findByName(name)
            .orElseGet(() -> {
                Role role = new Role();
                role.setName(name);
                role.setDescription(description);
                return roleRepository.save(role);
            });
    }

    private void ensureUser(String username, String email, String rawPassword, Role role) {
        if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            return;
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRoles(Set.of(role));

        userRepository.save(user);
        log.info("Default user created: username={}", username);
    }
}
