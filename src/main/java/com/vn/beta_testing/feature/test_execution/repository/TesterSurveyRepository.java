package com.vn.beta_testing.feature.test_execution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vn.beta_testing.domain.TesterSurvey;

@Repository
public interface TesterSurveyRepository extends JpaRepository<TesterSurvey, Long> {
    // Có thể thêm custom query sau, ví dụ: findByUserIdAndSurveyId(...)
}