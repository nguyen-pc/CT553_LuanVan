package com.vn.beta_testing.feature.survey_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.File;


@Repository
public interface FileRepository extends JpaRepository<com.vn.beta_testing.domain.File, Long>, JpaSpecificationExecutor<com.vn.beta_testing.domain.File> {
      List<File> findBySurvey_SurveyId(Long surveyId);

    Optional<File> findByFileName(String fileName);
}