package com.vn.beta_testing.feature.survey_service.repository;

import com.vn.beta_testing.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("""
        SELECT DISTINCT a FROM Answer a
        JOIN FETCH a.question q
        JOIN FETCH a.response r
        LEFT JOIN FETCH a.choices c
        WHERE q.questionId = :questionId
        """)
    List<Answer> findAllByQuestionId(@Param("questionId")Long questionId);
}