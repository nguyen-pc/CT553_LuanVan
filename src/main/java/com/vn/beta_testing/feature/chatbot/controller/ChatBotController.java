package com.vn.beta_testing.feature.chatbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

import com.vn.beta_testing.domain.ChatHistory;
import com.vn.beta_testing.domain.ChatSession;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.chatbot.DTO.AskRequest;
import com.vn.beta_testing.feature.chatbot.DTO.AskResponse;
import com.vn.beta_testing.feature.chatbot.repository.ChatHistoryRepository;
import com.vn.beta_testing.feature.chatbot.repository.ChatSessionRepository;
import com.vn.beta_testing.feature.chatbot.service.ChatService;
import com.vn.beta_testing.util.SecurityUtil;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatBotController {
    private final RestTemplate restTemplate;
    private final ChatService chatService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatSessionRepository chatSessionRepository;

    @Value("${chatbot.url}")
    private String chatbotUrl; // ví dụ: http://localhost:8000/chat

    public ChatBotController(RestTemplate restTemplate,
            ChatService chatService,
            ChatHistoryRepository chatHistoryRepository,
            ChatSessionRepository chatSessionRepository) {
        this.restTemplate = restTemplate;
        this.chatService = chatService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.chatSessionRepository = chatSessionRepository;
    }

    @PostMapping("/ask")
    public ResponseEntity<AskResponse> askBot(@RequestBody AskRequest body) {
        String sessionUuid = body.sessionId();
        String mode = (body.mode() == null || body.mode().isBlank()) ? "general" : body.mode();
        String message = body.message();
        Long userId = body.userId();

        ChatSession session = chatSessionRepository.findBySessionUuid(sessionUuid).orElse(null);
        if (sessionUuid == null || sessionUuid.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sessionUuid is required");
        }
        if (!chatService.sessionExists(sessionUuid)) {
            // Đảm bảo BE báo đúng khi FE quên tạo phiên
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
        }
        if (message == null || message.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message is required");
        }

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
        }

        if (!Objects.equals(session.getUserId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Session does not belong to user");
        }
        // Lịch sử hội thoại của phiên
        List<ChatHistory> histories = chatHistoryRepository.findBySession_SessionUuidOrderByCreatedAtAsc(sessionUuid);

        List<Map<String, String>> historyPayload = histories.stream()
                .map(h -> Map.of(
                        "role", (h.getQuestion() != null && !h.getQuestion().isBlank()) ? "user" : "assistant",
                        "content",
                        (h.getQuestion() != null && !h.getQuestion().isBlank()) ? h.getQuestion() : h.getAnswer()))
                .collect(Collectors.toList());

        // Payload gửi sang FastAPI
        Map<String, Object> payload = new HashMap<>();
        payload.put("sessionId", sessionUuid); // nếu FastAPI đang dùng "sessionId", giữ nguyên
        payload.put("mode", mode);
        payload.put("message", message);
        payload.put("history", historyPayload);

        // Gọi FastAPI
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);

        String answer;
        try {
            ResponseEntity<Map> resp = restTemplate.postForEntity(chatbotUrl, req, Map.class);
            System.out.println("AI service response: " + resp);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI service bad response");
            }
            Object ai = resp.getBody().get("response");
            answer = ai == null ? "" : ai.toString();
        } catch (Exception ex) {
            // Không làm app crash; trả lỗi gọn cho FE
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Cannot reach AI service", ex);
        }

        // Lưu lịch sử (user + assistant). Nếu bạn đã gộp trong service, giữ 1 call cũng
        // được.
        chatService.saveMessage(sessionUuid, mode, message, answer);

        return ResponseEntity.ok(new AskResponse(sessionUuid, answer));
    }
}