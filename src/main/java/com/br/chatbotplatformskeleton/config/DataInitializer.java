package com.br.chatbotplatformskeleton.config;

import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initializes default data (users, roles) on application startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

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
        // Create default admin user if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Get or create ADMIN role
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ADMIN");
                        role.setDescription("Administrador do sistema");
                        return roleRepository.save(role);
                    });

            // Create admin user
            UserAccount adminUser = new UserAccount();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@chatbot.local");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
            adminUser.setEnabled(true);
            adminUser.setRoles(Set.of(adminRole));

            userRepository.save(adminUser);
            System.out.println("✓ Default admin user created: username=admin, password=admin123");
        }

        // Create default test user if it doesn't exist
        if (userRepository.findByUsername("user").isEmpty()) {
            // Get or create USER role
            Role userRole = roleRepository.findByName("USUARIO")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("USUARIO");
                        role.setDescription("Usuário padrão");
                        return roleRepository.save(role);
                    });

            // Create test user
            UserAccount testUser = new UserAccount();
            testUser.setUsername("user");
            testUser.setEmail("user@chatbot.local");
            testUser.setPasswordHash(passwordEncoder.encode("user123"));
            testUser.setEnabled(true);
            testUser.setRoles(Set.of(userRole));

            userRepository.save(testUser);
            System.out.println("✓ Default test user created: username=user, password=user123");
        }

        System.out.println("✓ Application initialized successfully!");
    }
}

