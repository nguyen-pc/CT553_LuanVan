package com.vn.beta_testing.feature.chatbot.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.vn.beta_testing.domain.ChatSession;
import com.vn.beta_testing.feature.chatbot.repository.ChatHistoryRepository;
import com.vn.beta_testing.feature.chatbot.repository.ChatSessionRepository;
import com.vn.beta_testing.feature.chatbot.service.ChatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.*;

@RestController
@RequestMapping("/api/v1/chatbot/session")
public class ChatSessionController {
    private final ChatService chatService;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatSessionController(ChatService chatService, ChatSessionRepository chatSessionRepository, ChatHistoryRepository chatHistoryRepository) {
        this.chatService = chatService;
        this.chatSessionRepository = chatSessionRepository;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody Map<String, Object> req) {
        Long userId = ((Number) req.getOrDefault("userId", 0)).longValue();
        String topic = (String) req.getOrDefault("topic", "New Chat");
        // Trả về object chứa sessionUuid
        return ResponseEntity.ok(chatService.startSession(userId, topic));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveHistory(@RequestBody Map<String, Object> req) {
        String sessionUuid = (String) req.get("sessionUuid");
        String mode = (String) req.get("mode");
        String question = (String) req.get("question");
        String answer = (String) req.get("answer");
        return ResponseEntity.ok(chatService.saveMessage(sessionUuid, mode, question, answer));
    }

    @GetMapping("/{sessionUuid}/history")
    public ResponseEntity<?> getHistory(
            @PathVariable("sessionUuid") String sessionUuid,
            @RequestParam("userId") Long userId) {

        // 🧩 1️⃣ Kiểm tra session có tồn tại
        ChatSession session = chatSessionRepository.findBySessionUuid(sessionUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // 🧩 2️⃣ Kiểm tra session có thuộc user hiện tại không
        if (!Objects.equals(session.getUserId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Session does not belong to user");
        }

        // 🧩 3️⃣ Lấy lịch sử chat và trả trực tiếp
        return ResponseEntity.ok(chatService.getSessionHistory(sessionUuid));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listSessions(@RequestParam("userId") Long userId) {
        // chỉ trả session của user này
        List<ChatSession> sessions = chatSessionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // có thể map nhẹ để FE hiển thị (topic, createdAt, lastMessage)
        var result = sessions.stream().map(s -> {
            String last = chatHistoryRepository.findTop1BySessionOrderByCreatedAtDesc(s)
                    .map(h -> h.getAnswer() != null ? h.getAnswer() : h.getQuestion())
                    .orElse(null);
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("sessionUuid", s.getSessionUuid());
            m.put("topic", s.getTopic());
            m.put("createdAt", s.getCreatedAt().toString());
            m.put("lastMessage", last);
            return m;
        }).toList();

        return ResponseEntity.ok(result); // hoặc bọc ApiResponse
    }

}