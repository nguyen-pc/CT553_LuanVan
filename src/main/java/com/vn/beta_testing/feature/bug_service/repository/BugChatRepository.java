package com.vn.beta_testing.feature.bug_service.repository;

import com.vn.beta_testing.domain.BugChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BugChatRepository extends JpaRepository<BugChatMessage, Long> {
    List<BugChatMessage> findByBugReport_IdOrderByCreatedAtAsc(Long bugReportId);
}