package com.br.chatbotplatformskeleton.config;

import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
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
    private final boolean bootstrapDefaultUsers;
    private final boolean bootstrapSystemUser;
    private final String defaultAdminPassword;
    private final String defaultUserPassword;
    private final String systemUserPassword;

    public DataInitializer(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.bootstrap-default-users:false}") boolean bootstrapDefaultUsers,
        @Value("${app.bootstrap-system-user:true}") boolean bootstrapSystemUser,
        @Value("${app.default-admin.password:}") String defaultAdminPassword,
        @Value("${app.default-user.password:}") String defaultUserPassword,
        @Value("${app.system-user.password:}") String systemUserPassword
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapDefaultUsers = bootstrapDefaultUsers;
        this.bootstrapSystemUser = bootstrapSystemUser;
        this.defaultAdminPassword = defaultAdminPassword;
        this.defaultUserPassword = defaultUserPassword;
        this.systemUserPassword = systemUserPassword;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = ensureRole("ADMIN", "Administrador do sistema");
        ensureRole("GESTOR", "Gestor");
        Role userRole = ensureRole("USUARIO", "Usuario padrao");

        if (bootstrapDefaultUsers) {
            ensureUser("admin", "admin@chatbot.local", requireConfiguredPassword(defaultAdminPassword, "app.default-admin.password"), adminRole);
            ensureUser("user", "user@chatbot.local", requireConfiguredPassword(defaultUserPassword, "app.default-user.password"), userRole);
        }

        if (bootstrapSystemUser) {
            ensureSystemUser("bot.system", "bot@chatbot.local", requireConfiguredPassword(systemUserPassword, "app.system-user.password"), userRole);
        }

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

    private void ensureSystemUser(String username, String email, String rawPassword, Role role) {
        if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            return;
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        // System user must be enabled to send bot responses
        user.setEnabled(true);
        user.setRoles(Set.of(role));

        userRepository.save(user);
        log.info("System user created: username={}", username);
    }

    private String requireConfiguredPassword(String value, String propertyName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required bootstrap password property: " + propertyName);
        }
        return value;
    }
}
