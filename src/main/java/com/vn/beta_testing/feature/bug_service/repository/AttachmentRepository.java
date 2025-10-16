package com.vn.beta_testing.feature.bug_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.Attachment;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByBugReportId(Long bugId);
}