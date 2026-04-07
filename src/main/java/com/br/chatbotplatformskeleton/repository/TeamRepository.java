package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    Optional<Team> findByCode(String code);

    List<Team> findByDepartmentIdAndIsActiveTrue(Long departmentId);

    List<Team> findByTeamLeadIdAndIsActiveTrue(Long teamLeadId);

    @Query("SELECT t FROM Team t WHERE t.isActive = true AND (LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Team> searchByNameOrCode(@Param("search") String search, Pageable pageable);

    Page<Team> findByDepartmentIdAndIsActiveTrue(Long departmentId, Pageable pageable);

    @Query("SELECT t FROM Team t JOIN t.members u WHERE u.id = :userId AND t.isActive = true")
    List<Team> findTeamsByMemberId(@Param("userId") Long userId);

    @Query("SELECT t FROM Team t WHERE t.isActive = true")
    Page<Team> findAllActive(Pageable pageable);
}

