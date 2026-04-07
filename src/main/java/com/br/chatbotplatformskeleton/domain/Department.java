package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Department> subDepartments = new HashSet<>();

    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    private Set<UserAccount> users = new HashSet<>();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "manager_id")
    private Long managerId;

    private String location;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Department getParentDepartment() { return parentDepartment; }
    public void setParentDepartment(Department parentDepartment) { this.parentDepartment = parentDepartment; }

    public Set<Department> getSubDepartments() { return subDepartments; }
    public void setSubDepartments(Set<Department> subDepartments) { this.subDepartments = subDepartments; }

    public Set<UserAccount> getUsers() { return users; }
    public void setUsers(Set<UserAccount> users) { this.users = users; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
}

