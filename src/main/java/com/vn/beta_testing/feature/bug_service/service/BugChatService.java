package com.vn.beta_testing.feature.bug_service.service;

import com.vn.beta_testing.domain.BugChatMessage;
import com.vn.beta_testing.feature.bug_service.repository.BugChatMessageRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BugChatService {
    private final BugChatMessageRepository repository;

    public BugChatService(BugChatMessageRepository repository) {
        this.repository = repository;
    }

    public BugChatMessage sendMessage(BugChatMessage message) {
        return repository.save(message);
    }

    public List<BugChatMessage> getMessages(Long bugId) {
        return repository.findByBugReportIdOrderByCreatedAtAsc(bugId);
    }
}