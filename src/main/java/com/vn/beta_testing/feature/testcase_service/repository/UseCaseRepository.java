package com.vn.beta_testing.feature.testcase_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.TestScenario;
import com.vn.beta_testing.domain.UseCase;

@Repository
public interface UseCaseRepository extends JpaRepository<UseCase, Long>, JpaSpecificationExecutor<UseCase> {
    
}