package com.vn.beta_testing.feature.bug_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>,
        JpaSpecificationExecutor<Attachment> {
    // List<Attachment> findBySurvey_SurveyId(Long surveyId);

    Optional<Attachment> findByFileName(String fileName);
}