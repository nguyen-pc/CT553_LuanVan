package com.vn.beta_testing.feature.bug_service.service;

import com.vn.beta_testing.domain.BugChatMessage;
import com.vn.beta_testing.domain.BugReport;
import com.vn.beta_testing.feature.bug_service.DTO.BugChatMessageDTO;
import com.vn.beta_testing.feature.bug_service.controller.mapper.BugChatMapper;
import com.vn.beta_testing.feature.bug_service.repository.BugChatRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugChatService {

    private final BugChatRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public BugChatService(BugChatRepository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public BugChatMessageDTO sendMessage(Long bugId, BugChatMessage message) {
        BugReport bugReport = new BugReport();
        bugReport.setId(bugId);
        message.setBugReport(bugReport);
        message.setCreatedAt(LocalDateTime.now());

        BugChatMessage saved = repository.save(message);

        BugChatMessageDTO dto = BugChatMapper.toDTO(saved);

        // 🔥 Gửi realtime cho client
        messagingTemplate.convertAndSend("/topic/bugs/" + bugId, dto);

        return dto;
    }

    public List<BugChatMessageDTO> getMessages(Long bugId) {
        return repository.findByBugReport_IdOrderByCreatedAtAsc(bugId)
                .stream()
                .map(BugChatMapper::toDTO)
                .collect(Collectors.toList());
    }
}
