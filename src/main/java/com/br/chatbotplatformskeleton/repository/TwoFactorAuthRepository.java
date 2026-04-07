package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.TwoFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, Long> {

    Optional<TwoFactorAuth> findByUserId(Long userId);

    Optional<TwoFactorAuth> findByUserIdAndIsEnabledTrue(Long userId);
}

