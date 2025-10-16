package com.vn.beta_testing.feature.bug_service.controller.mapper;

import com.vn.beta_testing.domain.*;
import com.vn.beta_testing.feature.bug_service.DTO.BugReportDTO;


public class BugReportMapper {

    public static BugReport toEntity(BugReportDTO dto) {
        if (dto == null) return null;
        BugReport bug = new BugReport();
        // bug.setId(dto.getId());
        bug.setTitle(dto.getTitle());
        bug.setDescription(dto.getDescription());
        bug.setSeverity(dto.getSeverity());
        bug.setPriority(dto.getPriority());
        bug.setStatus(dto.getStatus());
        bug.setStepsToReproduce(dto.getStepsToReproduce());
        bug.setExpectedResult(dto.getExpectedResult());
        bug.setActualResult(dto.getActualResult());

        if (dto.getTesterId() != null) {
            User tester = new User();
            tester.setId(dto.getTesterId());
            bug.setTester(tester);
        }

        if (dto.getAssigneeId() != null) {
            User assignee = new User();
            assignee.setId(dto.getAssigneeId());
            bug.setAssignee(assignee);
        }

        if (dto.getBugTypeId() != null) {
            BugType type = new BugType();
            type.setId(dto.getBugTypeId());
            bug.setBugType(type);
        }

        if (dto.getCampaignId() != null) {
            Campaign campaign = new Campaign();
            campaign.setId(dto.getCampaignId());
            bug.setCampaign(campaign);
        }

        return bug;
    }

    public static BugReportDTO toDTO(BugReport entity) {
        if (entity == null) return null;
        BugReportDTO dto = new BugReportDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setSeverity(entity.getSeverity());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setStepsToReproduce(entity.getStepsToReproduce());
        dto.setExpectedResult(entity.getExpectedResult());
        dto.setActualResult(entity.getActualResult());

        if (entity.getTester() != null) dto.setTesterId(entity.getTester().getId());
        if (entity.getAssignee() != null) dto.setAssigneeId(entity.getAssignee().getId());
        if (entity.getBugType() != null) dto.setBugTypeId(entity.getBugType().getId());
        if (entity.getCampaign() != null) dto.setCampaignId(entity.getCampaign().getId());

        return dto;
    }
}