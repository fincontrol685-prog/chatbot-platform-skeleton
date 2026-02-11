package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
}
