package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByPasswordResetToken(String passwordResetToken);
}
