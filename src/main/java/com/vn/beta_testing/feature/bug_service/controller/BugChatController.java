package com.vn.beta_testing.feature.bug_service.controller;

import com.vn.beta_testing.domain.BugChatMessage;
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

    @PostMapping("/{bugId}/chat")
    public BugChatMessage sendMessage(@PathVariable Long bugId, @RequestBody BugChatMessage message) {
        // chỉ cần set bugReportId để map JPA tự động
        if (message.getBugReport() != null)
            message.getBugReport().setId(bugId);
        return service.sendMessage(message);
    }

    @GetMapping("/{bugId}/chat")
    public List<BugChatMessage> getMessages(@PathVariable Long bugId) {
        return service.getMessages(bugId);
    }
}