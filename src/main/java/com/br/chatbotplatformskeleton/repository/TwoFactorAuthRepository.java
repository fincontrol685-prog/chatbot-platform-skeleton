package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.TwoFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, Long> {

    Optional<TwoFactorAuth> findByUserId(Long userId);

    @Query("SELECT t FROM TwoFactorAuth t WHERE t.user.id = :userId AND t.isEnabled = true")
    Optional<TwoFactorAuth> findByUserIdAndIsEnabledTrue(@Param("userId") Long userId);
}

