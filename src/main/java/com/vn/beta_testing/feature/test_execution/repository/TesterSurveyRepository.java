package com.vn.beta_testing.feature.test_execution.repository;

import java.time.Instant;
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

    @Query("""
                SELECT COUNT(ts)
                FROM TesterSurvey ts
                WHERE ts.user.name = :name
                AND ts.survey.campaign.id = :campaignId
                AND ts.completed = true
            """)
    int countByNameAndCampaignId(@Param("name") String name, @Param("campaignId") Long campaignId);

    @Query("SELECT MONTH(ts.completionDate) AS month, COUNT(ts) AS total "
            + "FROM TesterSurvey ts WHERE ts.user.id = :userId "
            + "AND ts.completionDate >= :fromDate "
            + "GROUP BY MONTH(ts.completionDate) ORDER BY MONTH(ts.completionDate)")
    List<Object[]> countSurveysByMonth(@Param("userId") Long userId, @Param("fromDate") Instant fromDate);

    @Query("SELECT COUNT(ts) FROM TesterSurvey ts WHERE ts.user.id = :userId AND ts.completed = TRUE")
    Long countCompletedSurvey(@Param("userId") Long userId);
}