package com.vn.beta_testing.feature.chatbot.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.vn.beta_testing.domain.ChatSession;
import com.vn.beta_testing.domain.ChatHistory;
import com.vn.beta_testing.feature.chatbot.repository.ChatSessionRepository;
import com.vn.beta_testing.feature.chatbot.repository.ChatHistoryRepository;

@Service
public class ChatService {
    private final ChatSessionRepository sessionRepo;
    private final ChatHistoryRepository historyRepo;

    public ChatService(ChatSessionRepository sessionRepo, ChatHistoryRepository historyRepo) {
        this.sessionRepo = sessionRepo;
        this.historyRepo = historyRepo;
    }

    public ChatSession startSession(Long userId, String topic) {
        ChatSession session = new ChatSession();
        session.setSessionUuid(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setTopic(topic);
        return sessionRepo.save(session);
    }

    public ChatHistory saveMessage(String sessionUuid, String mode, String question, String answer) {
        ChatSession session = sessionRepo.findBySessionUuid(sessionUuid)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        ChatHistory h = new ChatHistory();
        h.setSession(session);
        h.setMode(mode);
        h.setQuestion(question);
        h.setAnswer(answer);
        return historyRepo.save(h);
    }

    public List<ChatHistory> getSessionHistory(String sessionUuid) {
        ChatSession session = sessionRepo.findBySessionUuid(sessionUuid)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return historyRepo.findBySession_Id(session.getId());
    }

    public boolean sessionExists(String sessionUuid) {
        if (sessionUuid == null || sessionUuid.isBlank()) {
            return false;
        }
        return sessionRepo.existsBySessionUuid(sessionUuid);
    }
}