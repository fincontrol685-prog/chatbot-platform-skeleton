package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.CustomReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomReportRepository extends JpaRepository<CustomReport, Long> {

    Optional<CustomReport> findByName(String name);

    @Query("SELECT cr FROM CustomReport cr WHERE cr.owner.id = :userId OR cr.isPublic = true ORDER BY cr.updatedAt DESC")
    Page<CustomReport> findAccessibleReports(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT cr FROM CustomReport cr WHERE cr.owner.id = :userId ORDER BY cr.updatedAt DESC")
    Page<CustomReport> findByOwnerId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT cr FROM CustomReport cr WHERE cr.isPublic = true ORDER BY cr.updatedAt DESC")
    List<CustomReport> findPublicReports();
}

