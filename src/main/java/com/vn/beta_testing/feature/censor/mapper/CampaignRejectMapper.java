package com.vn.beta_testing.feature.censor.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.vn.beta_testing.domain.CampaignRejectReason;
import com.vn.beta_testing.feature.censor.DTO.CampaignRejectMessageDTO;
import com.vn.beta_testing.feature.censor.DTO.CampaignRejectReasonDTO;
import com.vn.beta_testing.domain.CampaignRejectMessage;


@Component
public class CampaignRejectMapper {

    public CampaignRejectMessageDTO toMessageDTO(CampaignRejectMessage entity) {
        if (entity == null) return null;

        CampaignRejectMessageDTO dto = new CampaignRejectMessageDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setSenderId(entity.getSenderId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        return dto;
    }

    public CampaignRejectReasonDTO toReasonDTO(CampaignRejectReason entity) {
        if (entity == null) return null;

        CampaignRejectReasonDTO dto = new CampaignRejectReasonDTO();
        dto.setId(entity.getId());
        dto.setInitialReason(entity.getInitialReason());
        dto.setCampaignId(entity.getCampaign() != null ? entity.getCampaign().getId() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        if (entity.getMessages() != null) {
            dto.setMessages(entity.getMessages().stream()
                    .map(this::toMessageDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public List<CampaignRejectReasonDTO> toReasonDTOList(List<CampaignRejectReason> list) {
        return list.stream().map(this::toReasonDTO).collect(Collectors.toList());
    }
}