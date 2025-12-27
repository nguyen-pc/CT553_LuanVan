package com.vn.beta_testing.feature.chatbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

import com.vn.beta_testing.domain.ChatHistory;
import com.vn.beta_testing.domain.ChatSession;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.bug_service.repository.BugReportRepository;
import com.vn.beta_testing.feature.bug_service.service.BugReportService;
import com.vn.beta_testing.feature.chatbot.DTO.AskRequest;
import com.vn.beta_testing.feature.chatbot.DTO.AskResponse;
import com.vn.beta_testing.feature.chatbot.repository.ChatHistoryRepository;
import com.vn.beta_testing.feature.chatbot.repository.ChatSessionRepository;
import com.vn.beta_testing.feature.chatbot.service.ChatService;
import com.vn.beta_testing.feature.company_service.repository.CampaignRepository;
import com.vn.beta_testing.feature.survey_service.repository.SurveyRepository;
import com.vn.beta_testing.feature.test_execution.service.TesterSurveyService;
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
    private final BugReportRepository bugRepository;
    private final BugReportService bugReportService;
    private final CampaignRepository campaignRepository;
    private final SurveyRepository feedbackRepository;
    private final TesterSurveyService testerSurveyService;

    @Value("${chatbot.url}")
    private String chatbotUrl; // ví dụ: http://localhost:8000/chat

    public ChatBotController(RestTemplate restTemplate,
            ChatService chatService,
            ChatHistoryRepository chatHistoryRepository,
            ChatSessionRepository chatSessionRepository,
            BugReportRepository bugRepository,
            BugReportService bugReportService,
            CampaignRepository campaignRepository,
            SurveyRepository feedbackRepository,
            TesterSurveyService testerSurveyService) {
        this.restTemplate = restTemplate;
        this.chatService = chatService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.bugRepository = bugRepository;
        this.campaignRepository = campaignRepository;
        this.feedbackRepository = feedbackRepository;
        this.bugReportService = bugReportService;
        this.testerSurveyService = testerSurveyService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AskResponse> askBot(@RequestBody AskRequest body) {
        String sessionUuid = body.sessionId();
        String mode = (body.mode() == null || body.mode().isBlank()) ? "general" : body.mode();
        String message = body.message();
        Long userId = body.userId();
        Long campaignId = body.campaignId();
        Long surveyId = 9L;

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

        // Phân tích intent từ message
        IntentResult intent = detectIntent(message);
        Map<String, Object> contextData = new HashMap<>();

        if (!"general".equals(mode)) {
            contextData = Collections.emptyMap();
        } else {
            //  Query DB tùy theo intent 
            switch (intent.intent()) {
                case "BUG_OPEN_LIST" -> {
                    contextData.put("bugs", bugReportService.getByCampaign(campaignId));
                }

                case "BUG_BY_SEVERITY" -> {
                    String severity = (String) intent.filters().get("severity");
                    contextData.put("bugs", bugReportService.getByCampaign(campaignId));
                }

                case "BUG_BY_MODULE" -> {
                    String module = (String) intent.filters().get("module");
                    contextData.put("bugs", bugReportService.getByCampaign(campaignId));
                }

                case "BUG_DETAIL" -> {
                    Long bugId = (Long) intent.filters().get("bugId");
                    contextData.put("bug", bugReportService.getById(bugId));
                }

                case "BUG_SUMMARY_AI" -> {
                    Long bugId = (Long) intent.filters().get("bugId");
                    contextData.put("bug", bugReportService.getById(bugId)); // GPT sẽ tóm tắt dựa trên dữ liệu thực
                }

                case "BUG_FIX_SUGGESTION" -> {
                    contextData.put("bugs", bugReportService.getByCampaign(campaignId));
                }

                case "FEEDBACK_ANALYSIS" -> {
                    contextData.put("feedback", testerSurveyService.getBySurveyId(surveyId));
                }

                case "CAMPAIGN_PROGRESS" -> {
                    contextData.put("progress", campaignRepository.findById(campaignId));
                }

                case "IMPROVE_TEST_PROCESS" -> {
                    contextData.put("stats", campaignRepository.findById(campaignId));
                }

            }
        }

        // Payload gửi sang FastAPI
        Map<String, Object> payload = new HashMap<>();
        payload.put("sessionId", sessionUuid); // nếu FastAPI đang dùng "sessionId", giữ nguyên
        payload.put("mode", mode);
        payload.put("message", message);
        payload.put("history", historyPayload);
        payload.put("contextData", contextData);

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

        // Lưu lịch sử (user + assistant).
        chatService.saveMessage(sessionUuid, mode, message, answer);

        return ResponseEntity.ok(new AskResponse(sessionUuid, answer));
    }

    public record IntentResult(String intent, Map<String, Object> filters) {
    }

    private IntentResult detectIntent(String msg) {
        if (msg == null || msg.isBlank()) {
            return new IntentResult("NONE", null);
        }

        String text = msg.toLowerCase();

        // Lấy danh sách bug
        if (text.contains("bug") && text.contains("đang mở")
                || text.contains("open bug")
                || text.contains("bug chưa fix")
                || text.contains("toàn bộ bug")
                || text.contains("ds bug")
                || text.contains("danh sách bug")
                || text.contains("liệt kê bug")
                || text.contains("list bug")) {
            return new IntentResult("BUG_OPEN_LIST", null);
        }

        // Bug theo severity
        // if (text.contains("bug") && (text.contains("critical") ||
        // text.contains("nghiêm trọng"))) {
        // return new IntentResult("BUG_BY_SEVERITY", Map.of("severity", "CRITICAL"));
        // }
        // if (text.contains("bug") && text.contains("high")) {
        // return new IntentResult("BUG_BY_SEVERITY", Map.of("severity", "HIGH"));
        // }
        // if (text.contains("bug") && text.contains("medium")) {
        // return new IntentResult("BUG_BY_SEVERITY", Map.of("severity", "MEDIUM"));
        // }
        // if (text.contains("bug") && text.contains("low")) {
        // return new IntentResult("BUG_BY_SEVERITY", Map.of("severity", "LOW"));
        // }

        // Bug theo module
        if (text.contains("bug") && text.contains("module")) {
            String moduleName = extractModuleName(text);
            return new IntentResult("BUG_BY_MODULE", Map.of("module", moduleName));
        }

        // 2. Xem chi tiết bug
        if (text.contains("chi tiết bug") || text.contains("bug") && text.matches(".*\\d+.*")) {
            Long bugId = extractBugId(text);
            return new IntentResult("BUG_DETAIL", Map.of("bugId", bugId));
        }

        // 3. Tóm tắt bug bằng AI
        if (text.contains("tóm tắt") && text.contains("bug")) {
            Long bugId = extractBugId(text);
            return new IntentResult("BUG_SUMMARY_AI", Map.of("bugId", bugId));
        }

        // 4. Gợi ý ưu tiên sửa lỗi
        if (text.contains("ưu tiên") && text.contains("bug")) {
            return new IntentResult("BUG_FIX_SUGGESTION", null);
        }

        // 5. Phân tích feedback tester
        if (text.contains("phân tích feedback") || text.contains("đánh giá phản hồi") || text.contains("phản hồi") 
                || text.contains("phân tích phản hồi")) {
            return new IntentResult("FEEDBACK_ANALYSIS", null);
        }

        // 6. Tiến độ chiến dịch
        if (text.contains("tiến độ") || text.contains("progress") || text.contains("tình hình kiểm thử")) {
            return new IntentResult("CAMPAIGN_PROGRESS", null);
        }

        // 7. Gợi ý cải thiện test coverage
        if (text.contains("cải thiện kiểm thử") || text.contains("test coverage")
                || text.contains("quy trình kiểm thử")) {
            return new IntentResult("IMPROVE_TEST_PROCESS", null);
        }

        return new IntentResult("NONE", null);
    }

    private Long extractBugId(String text) {
        try {
            String id = text.replaceAll("\\D+", "");
            return Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractModuleName(String text) {
        // ví dụ: "bug trong module payment"
        if (text.contains("module")) {
            int idx = text.indexOf("module");
            return text.substring(idx + 7).trim();
        }
        return null;
    }

}