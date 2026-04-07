package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Team;
import com.br.chatbotplatformskeleton.domain.TeamRole;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.TeamDto;
import com.br.chatbotplatformskeleton.repository.DepartmentRepository;
import com.br.chatbotplatformskeleton.repository.TeamRepository;
import com.br.chatbotplatformskeleton.repository.TeamRoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamRoleRepository teamRoleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;

    public TeamService(TeamRepository teamRepository,
                       TeamRoleRepository teamRoleRepository,
                       UserRepository userRepository,
                       DepartmentRepository departmentRepository,
                       AuditService auditService) {
        this.teamRepository = teamRepository;
        this.teamRoleRepository = teamRoleRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.auditService = auditService;
    }

    public TeamDto create(TeamDto dto, Long userId) {
        Team team = new Team();
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());
        team.setCode(dto.getCode());
        team.setMaxConversationsPerUser(dto.getMaxConversationsPerUser());
        team.setIsActive(true);
        team.setCreatedBy(userId);
        team.setUpdatedBy(userId);
        team.setCreatedAt(OffsetDateTime.now());
        team.setUpdatedAt(OffsetDateTime.now());

        if (dto.getDepartmentId() != null) {
            departmentRepository.findById(dto.getDepartmentId()).ifPresent(team::setDepartment);
        }

        Team saved = teamRepository.save(team);

        // Assign team lead if provided
        if (dto.getTeamLeadId() != null) {
            assignTeamLead(saved.getId(), dto.getTeamLeadId(), userId);
        }

        // Add members if provided
        if (dto.getMemberIds() != null && !dto.getMemberIds().isEmpty()) {
            dto.getMemberIds().forEach(memberId -> addMember(saved.getId(), memberId, userId));
        }

        auditService.log(userId, "CREATE", "TEAM", saved.getId(), null, saved.getName());
        return toDto(saved);
    }

    public TeamDto update(Long id, TeamDto dto, Long userId) {
        Optional<Team> optional = teamRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Team not found: " + id);
        }

        Team team = optional.get();
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());
        team.setCode(dto.getCode());
        team.setMaxConversationsPerUser(dto.getMaxConversationsPerUser());
        team.setUpdatedBy(userId);
        team.setUpdatedAt(OffsetDateTime.now());

        Team updated = teamRepository.save(team);
        auditService.log(userId, "UPDATE", "TEAM", updated.getId(), null, updated.getName());
        return toDto(updated);
    }

    public Optional<TeamDto> findById(Long id) {
        return teamRepository.findById(id).map(this::toDto);
    }

    public Page<TeamDto> listByDepartment(Long departmentId, Pageable pageable) {
        Page<Team> page = teamRepository.findByDepartmentIdAndIsActiveTrue(departmentId, pageable);
        return page.map(this::toDto);
    }

    public Page<TeamDto> listActive(Pageable pageable) {
        Page<Team> page = teamRepository.findAllActive(pageable);
        return page.map(this::toDto);
    }

    public Page<TeamDto> search(String search, Pageable pageable) {
        Page<Team> page = teamRepository.searchByNameOrCode(search, pageable);
        return page.map(this::toDto);
    }

    public void addMember(Long teamId, Long userId, Long addedBy) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        Optional<UserAccount> userOpt = userRepository.findById(userId);

        if (teamOpt.isPresent() && userOpt.isPresent()) {
            Team team = teamOpt.get();
            UserAccount user = userOpt.get();
            team.getMembers().add(user);
            teamRepository.save(team);

            // Create team role
            TeamRole role = new TeamRole();
            role.setTeam(team);
            role.setUser(user);
            role.setRole("MEMBER");
            role.setAssignedBy(addedBy);
            role.setAssignedAt(OffsetDateTime.now());
            teamRoleRepository.save(role);

            auditService.log(addedBy, "ADD_MEMBER", "TEAM", teamId, null, user.getUsername());
        }
    }

    public void removeMember(Long teamId, Long userId, Long removedBy) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        Optional<UserAccount> userOpt = userRepository.findById(userId);

        if (teamOpt.isPresent() && userOpt.isPresent()) {
            Team team = teamOpt.get();
            UserAccount user = userOpt.get();
            team.getMembers().remove(user);
            teamRepository.save(team);

            // Deactivate team role
            Optional<TeamRole> roleOpt = teamRoleRepository.findByTeamIdAndUserIdAndIsActiveTrue(teamId, userId);
            roleOpt.ifPresent(role -> {
                role.setIsActive(false);
                teamRoleRepository.save(role);
            });

            auditService.log(removedBy, "REMOVE_MEMBER", "TEAM", teamId, null, user.getUsername());
        }
    }

    public void assignTeamLead(Long teamId, Long userId, Long assignedBy) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        Optional<UserAccount> userOpt = userRepository.findById(userId);

        if (teamOpt.isPresent() && userOpt.isPresent()) {
            Team team = teamOpt.get();
            team.setTeamLeadId(userId);
            teamRepository.save(team);

            // Create or update team role
            Optional<TeamRole> existing = teamRoleRepository.findByTeamIdAndUserIdAndIsActiveTrue(teamId, userId);
            if (existing.isPresent()) {
                existing.get().setRole("TEAM_LEAD");
                teamRoleRepository.save(existing.get());
            } else {
                TeamRole role = new TeamRole();
                role.setTeam(team);
                role.setUser(userOpt.get());
                role.setRole("TEAM_LEAD");
                role.setAssignedBy(assignedBy);
                role.setAssignedAt(OffsetDateTime.now());
                teamRoleRepository.save(role);
            }

            auditService.log(assignedBy, "ASSIGN_LEAD", "TEAM", teamId, null, userOpt.get().getUsername());
        }
    }

    public void delete(Long id, Long userId) {
        Optional<Team> optional = teamRepository.findById(id);
        if (optional.isPresent()) {
            Team team = optional.get();
            team.setIsActive(false);
            team.setUpdatedBy(userId);
            team.setUpdatedAt(OffsetDateTime.now());
            teamRepository.save(team);
            auditService.log(userId, "DELETE", "TEAM", id, null, "deactivated");
        }
    }

    private TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        dto.setCode(team.getCode());
        dto.setIsActive(team.getIsActive());
        dto.setMaxConversationsPerUser(team.getMaxConversationsPerUser());
        dto.setCreatedAt(team.getCreatedAt());
        dto.setUpdatedAt(team.getUpdatedAt());
        dto.setTeamLeadId(team.getTeamLeadId());
        if (team.getDepartment() != null) {
            dto.setDepartmentId(team.getDepartment().getId());
            dto.setDepartmentName(team.getDepartment().getName());
        }
        if (team.getMembers() != null) {
            dto.setMemberIds(team.getMembers().stream()
                .map(UserAccount::getId)
                .collect(Collectors.toSet()));
        }
        return dto;
    }
}

