package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    Optional<Department> findByCode(String code);

    @Query("SELECT d FROM Department d WHERE d.isActive = true")
    List<Department> findByIsActiveTrue();

    List<Department> findByParentDepartmentId(Long parentDepartmentId);

    @Query("SELECT d FROM Department d WHERE d.parentDepartment IS NULL AND d.isActive = true")
    List<Department> findRootDepartments();

    @Query("SELECT d FROM Department d WHERE d.managerId = :managerId AND d.isActive = true")
    List<Department> findByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT d FROM Department d WHERE d.isActive = true")
    Page<Department> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT d FROM Department d WHERE d.isActive = true AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY d.createdAt DESC")
    Page<Department> searchByNameOrCode(@Param("search") String search, Pageable pageable);
}

