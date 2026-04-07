package com.br.chatbotplatformskeleton.repository;

import com.br.chatbotplatformskeleton.domain.DataDeletionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataDeletionRequestRepository extends JpaRepository<DataDeletionRequest, Long> {

    Optional<DataDeletionRequest> findByUserIdAndStatus(Long userId, String status);

    List<DataDeletionRequest> findByUserId(Long userId);

    Page<DataDeletionRequest> findByStatus(String status, Pageable pageable);

    Page<DataDeletionRequest> findAll(Pageable pageable);
}

