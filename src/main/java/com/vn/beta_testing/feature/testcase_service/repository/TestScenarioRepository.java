package com.vn.beta_testing.feature.testcase_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.TestScenario;

@Repository
public interface TestScenarioRepository extends JpaRepository<TestScenario, Long>, JpaSpecificationExecutor<TestScenario> {
    
}