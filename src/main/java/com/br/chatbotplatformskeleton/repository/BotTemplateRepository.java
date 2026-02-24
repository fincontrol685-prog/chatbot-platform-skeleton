package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.BotTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotTemplateRepository extends JpaRepository<BotTemplate, Long> {

    Page<BotTemplate> findByIsPublicTrue(Pageable pageable);

    Page<BotTemplate> findByCategory(String category, Pageable pageable);

    Page<BotTemplate> findByCreatedById(Long userId, Pageable pageable);

    @Query("SELECT t FROM BotTemplate t WHERE t.isPublic = true ORDER BY t.usageCount DESC")
    Page<BotTemplate> findMostUsedPublicTemplates(Pageable pageable);

    @Query("SELECT t FROM BotTemplate t WHERE t.isPublic = true AND t.rating IS NOT NULL ORDER BY t.rating DESC")
    Page<BotTemplate> findTopRatedTemplates(Pageable pageable);

    @Query("SELECT t FROM BotTemplate t WHERE t.category = :category AND t.isPublic = true ORDER BY t.usageCount DESC")
    List<BotTemplate> findByCategory(@Param("category") String category);
}

