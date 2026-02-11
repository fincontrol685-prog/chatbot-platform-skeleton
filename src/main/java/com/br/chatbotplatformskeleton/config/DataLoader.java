package com.br.chatbotplatformskeleton.config;

import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner load(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role admin = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(createRole("ADMIN", "Administrador")));
            Role gestor = roleRepository.findByName("GESTOR").orElseGet(() -> roleRepository.save(createRole("GESTOR", "Gestor")));
            Role usuario = roleRepository.findByName("USUARIO").orElseGet(() -> roleRepository.save(createRole("USUARIO", "Usuário")));

            if (userRepository.findByUsername("admin").isEmpty()) {
                UserAccount u = new UserAccount();
                u.setUsername("admin");
                u.setEmail("admin@example.com");
                u.setPasswordHash(passwordEncoder.encode("admin"));
                Set<Role> roles = new HashSet<>();
                roles.add(admin);
                u.setRoles(roles);
                userRepository.save(u);
            }
        };
    }

    private Role createRole(String name, String desc) { Role r = new Role(); r.setName(name); r.setDescription(desc); return r; }
}
