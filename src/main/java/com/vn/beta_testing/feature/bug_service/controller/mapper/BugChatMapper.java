package com.vn.beta_testing.feature.bug_service.controller.mapper;

import com.vn.beta_testing.domain.BugChatMessage;
import com.vn.beta_testing.feature.bug_service.DTO.BugChatMessageDTO;

public class BugChatMapper {

    public static BugChatMessageDTO toDTO(BugChatMessage entity) {
        BugChatMessageDTO dto = new BugChatMessageDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setInternal(entity.isInternal());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getSender() != null) {
            dto.setSenderId(entity.getSender().getId());
            dto.setSenderEmail(entity.getSender().getEmail());
            dto.setSenderName(entity.getSender().getName());
        }

        if (entity.getBugReport() != null) {
            dto.setBugId(entity.getBugReport().getId());
        }

        return dto;
    }
}