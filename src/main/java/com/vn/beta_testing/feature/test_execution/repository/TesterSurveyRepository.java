package com.vn.beta_testing.feature.test_execution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.vn.beta_testing.domain.TesterSurvey;

@Repository
public interface TesterSurveyRepository extends JpaRepository<TesterSurvey, Long> {
    // Có thể thêm custom query sau, ví dụ: findByUserIdAndSurveyId(...)
    Optional<TesterSurvey> findByUserIdAndSurvey_SurveyId(Long userId, Long surveyId);

    List<TesterSurvey> findBySurvey_SurveyId(Long surveyId);

    @Query("SELECT ts FROM TesterSurvey ts " +
            "JOIN FETCH ts.user u " +
            "JOIN FETCH ts.response r " +
            "WHERE r.responseId = :responseId")
    Optional<TesterSurvey> findByResponseId(@Param("responseId") Long responseId);
}