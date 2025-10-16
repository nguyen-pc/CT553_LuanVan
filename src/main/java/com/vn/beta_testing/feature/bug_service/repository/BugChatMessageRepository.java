package com.vn.beta_testing.feature.bug_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.BugChatMessage;

public interface BugChatMessageRepository extends JpaRepository<BugChatMessage, Long> {
    List<BugChatMessage> findByBugReportIdOrderByCreatedAtAsc(Long bugId);
}