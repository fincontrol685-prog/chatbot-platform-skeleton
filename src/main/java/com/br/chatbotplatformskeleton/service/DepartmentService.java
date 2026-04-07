package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Department;
import com.br.chatbotplatformskeleton.dto.DepartmentDto;
import com.br.chatbotplatformskeleton.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;

    public DepartmentService(DepartmentRepository departmentRepository, AuditService auditService) {
        this.departmentRepository = departmentRepository;
        this.auditService = auditService;
    }

    public DepartmentDto create(DepartmentDto dto, Long userId) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setCode(dto.getCode());
        department.setLocation(dto.getLocation());
        department.setManagerId(dto.getManagerId());
        department.setIsActive(true);
        department.setCreatedBy(userId);
        department.setUpdatedBy(userId);
        department.setCreatedAt(OffsetDateTime.now());
        department.setUpdatedAt(OffsetDateTime.now());

        if (dto.getParentDepartmentId() != null) {
            Optional<Department> parent = departmentRepository.findById(dto.getParentDepartmentId());
            parent.ifPresent(department::setParentDepartment);
        }

        Department saved = departmentRepository.save(department);
        auditService.log(userId, "CREATE", "DEPARTMENT", saved.getId(), null, saved.getName());
        return toDto(saved);
    }

    public DepartmentDto update(Long id, DepartmentDto dto, Long userId) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Department not found: " + id);
        }

        Department department = optional.get();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setCode(dto.getCode());
        department.setLocation(dto.getLocation());
        department.setManagerId(dto.getManagerId());
        department.setUpdatedBy(userId);
        department.setUpdatedAt(OffsetDateTime.now());

        Department updated = departmentRepository.save(department);
        auditService.log(userId, "UPDATE", "DEPARTMENT", updated.getId(), null, updated.getName());
        return toDto(updated);
    }

    public Optional<DepartmentDto> findById(Long id) {
        return departmentRepository.findById(id).map(this::toDto);
    }

    public Page<DepartmentDto> listActive(Pageable pageable) {
        Page<Department> page = departmentRepository.findByIsActiveTrue(pageable);
        return page.map(this::toDto);
    }

    public Page<DepartmentDto> search(String search, Pageable pageable) {
        Page<Department> page = departmentRepository.searchByNameOrCode(search, pageable);
        return page.map(this::toDto);
    }

    public List<DepartmentDto> getRootDepartments() {
        return departmentRepository.findRootDepartments().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<DepartmentDto> getSubDepartments(Long parentId) {
        return departmentRepository.findByParentDepartmentId(parentId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public void delete(Long id, Long userId) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isPresent()) {
            Department department = optional.get();
            department.setIsActive(false);
            department.setUpdatedBy(userId);
            department.setUpdatedAt(OffsetDateTime.now());
            departmentRepository.save(department);
            auditService.log(userId, "DELETE", "DEPARTMENT", id, null, "deactivated");
        }
    }

    public void assign(Long departmentId, Long userId, Long assignedBy) {
        // This method is for assigning users to departments (bidirectional relationship)
        // Implementation handled in UserService
    }

    private DepartmentDto toDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setCode(department.getCode());
        dto.setLocation(department.getLocation());
        dto.setManagerId(department.getManagerId());
        dto.setIsActive(department.getIsActive());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());
        if (department.getParentDepartment() != null) {
            dto.setParentDepartmentId(department.getParentDepartment().getId());
        }
        if (department.getUsers() != null) {
            dto.setUserIds(department.getUsers().stream()
                .map(u -> u.getId())
                .collect(Collectors.toSet()));
        }
        return dto;
    }
}

