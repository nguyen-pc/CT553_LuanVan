package com.vn.beta_testing.feature.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vn.beta_testing.domain.ChatHistory;
import com.vn.beta_testing.domain.ChatSession;

import java.util.List;
import java.util.Optional;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findBySession_Id(Long sessionId);

    List<ChatHistory> findBySession_SessionUuidOrderByCreatedAtAsc(String sessionUuid);

    // (Tuỳ chọn) Lấy lịch sử mới nhất theo session
    List<ChatHistory> findTop5BySession_SessionUuidOrderByCreatedAtDesc(String sessionUuid);

    Optional<ChatHistory> findTop1BySessionOrderByCreatedAtDesc(ChatSession session);

}