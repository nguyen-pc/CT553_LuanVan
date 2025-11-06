package com.vn.beta_testing.feature.company_service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.ProjectUser;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    List<ProjectUser> findByProjectId(Long projectId);
    Optional<ProjectUser> findByProjectIdAndUserId(Long projectId, Long userId);
}