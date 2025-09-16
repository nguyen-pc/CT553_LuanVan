package com.vn.beta_testing.feature.survey_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Question;
import com.vn.beta_testing.domain.Survey;



@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    List<Question> findBySurvey_SurveyId(long surveyId);
}