package com.vn.beta_testing.feature.censor.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.CampaignRejectReason;
import com.vn.beta_testing.feature.censor.DTO.CampaignRejectMessageDTO;
import com.vn.beta_testing.feature.censor.DTO.CampaignRejectReasonDTO;
import com.vn.beta_testing.feature.censor.mapper.CampaignRejectMapper;
import com.vn.beta_testing.feature.censor.repository.CampaignRejectMessageRepository;
import com.vn.beta_testing.feature.censor.repository.CampaignRejectReasonRepository;
import com.vn.beta_testing.feature.company_service.repository.CampaignRepository;
import com.vn.beta_testing.domain.CampaignRejectMessage;


@Service
@RequiredArgsConstructor
public class CampaignRejectService {

    private final CampaignRepository campaignRepository;
    private final CampaignRejectReasonRepository reasonRepository;
    private final CampaignRejectMessageRepository messageRepository;
    private final CampaignRejectMapper mapper;

    // 🟠 Tạo mới lý do từ chối
    public CampaignRejectReasonDTO createRejectReason(Long campaignId, String initialReason) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Campaign với ID = " + campaignId));

        CampaignRejectReason reason = new CampaignRejectReason();
        reason.setCampaign(campaign);
        reason.setInitialReason(initialReason);

        CampaignRejectReason saved = reasonRepository.save(reason);
        return mapper.toReasonDTO(saved);
    }

    // 🟠 Lấy tất cả lý do từ chối của campaign
    public List<CampaignRejectReasonDTO> getRejectReasons(Long campaignId) {
        List<CampaignRejectReason> reasons = reasonRepository.findByCampaignIdOrderByCreatedAtDesc(campaignId);
        return mapper.toReasonDTOList(reasons);
    }

    // 🟠 Thêm phản hồi chat
    public CampaignRejectMessageDTO addMessage(Long reasonId, Long senderId, String content) {
        CampaignRejectReason reason = reasonRepository.findById(reasonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lý do từ chối với ID = " + reasonId));

        CampaignRejectMessage message = new CampaignRejectMessage();
        message.setRejectReason(reason);
        message.setSenderId(senderId);
        message.setContent(content);

        CampaignRejectMessage saved = messageRepository.save(message);
        return mapper.toMessageDTO(saved);
    }

    // 🟠 Lấy danh sách phản hồi chat theo lý do
    public List<CampaignRejectMessageDTO> getMessages(Long reasonId) {
        return messageRepository.findByRejectReasonIdOrderByCreatedAtAsc(reasonId)
                .stream()
                .map(mapper::toMessageDTO)
                .toList();
    }
}