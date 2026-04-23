package com.br.chatbotplatformskeleton.security;

import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("Credenciais invalidas"));
        Set<GrantedAuthority> authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).collect(Collectors.toSet());
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getEnabled())
                .build();
    }
}
