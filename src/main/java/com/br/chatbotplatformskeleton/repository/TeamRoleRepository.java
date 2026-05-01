package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRoleRepository extends JpaRepository<TeamRole, Long> {

    @Query("SELECT tr FROM TeamRole tr WHERE tr.team.id = :teamId AND tr.isActive = true")
    List<TeamRole> findByTeamIdAndIsActiveTrue(@Param("teamId") Long teamId);

    @Query("SELECT tr FROM TeamRole tr WHERE tr.user.id = :userId AND tr.isActive = true")
    List<TeamRole> findByUserIdAndIsActiveTrue(@Param("userId") Long userId);

    @Query("SELECT tr FROM TeamRole tr WHERE tr.team.id = :teamId AND tr.user.id = :userId AND tr.isActive = true")
    Optional<TeamRole> findByTeamIdAndUserIdAndIsActiveTrue(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Query("SELECT tr FROM TeamRole tr WHERE tr.team.id = :teamId AND tr.role = :role AND tr.isActive = true")
    Optional<TeamRole> findTeamLeadByTeamId(@Param("teamId") Long teamId, @Param("role") String role);

    @Query("SELECT tr.user.id FROM TeamRole tr WHERE tr.team.id = :teamId AND tr.isActive = true")
    List<Long> findMemberIdsByTeamId(@Param("teamId") Long teamId);
}

