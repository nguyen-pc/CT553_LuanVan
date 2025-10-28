package com.vn.beta_testing.feature.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.ChatHistory;
import com.vn.beta_testing.domain.ChatSession;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findBySessionUuid(String uuid);

    boolean existsBySessionUuid(String sessionUuid);

    List<ChatSession> findByUserIdOrderByCreatedAtDesc(Long userId);

  
}