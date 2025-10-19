package com.vn.beta_testing.feature.bug_service.controller;


import com.vn.beta_testing.domain.BugChatMessage;
import com.vn.beta_testing.feature.bug_service.DTO.BugChatMessageDTO;
import com.vn.beta_testing.feature.bug_service.service.BugChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bugs")
public class BugChatController {

    private final BugChatService service;

    public BugChatController(BugChatService service) {
        this.service = service;
    }

    // 📨 Gửi tin nhắn (REST + broadcast realtime)
    @PostMapping("/{bugId}/chat")
    public BugChatMessageDTO sendMessage(
            @PathVariable("bugId") Long bugId,
            @RequestBody BugChatMessage message) {
        return service.sendMessage(bugId, message);
    }

    // 📜 Lấy lịch sử chat
    @GetMapping("/{bugId}/chat")
    public List<BugChatMessageDTO> getMessages(@PathVariable("bugId") Long bugId) {
        return service.getMessages(bugId);
    }
}
