package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByUsernameIgnoreCase(String username);
    Optional<UserAccount> findByEmailIgnoreCase(String email);
    Optional<UserAccount> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    Optional<UserAccount> findByPasswordResetToken(String passwordResetToken);
}
