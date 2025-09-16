package com.vn.beta_testing.feature.survey_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long>, JpaSpecificationExecutor<Response> {
     List<Response> findBySurvey_SurveyId(Long surveyId);
}