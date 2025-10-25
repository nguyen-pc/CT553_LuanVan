package com.vn.beta_testing.feature.email_service.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.EmailTester;
import com.vn.beta_testing.feature.email_service.DTO.EmailTesterDTO;
import com.vn.beta_testing.feature.email_service.repository.EmailTesterRepository;
import com.vn.beta_testing.feature.company_service.repository.CampaignRepository;
import com.vn.beta_testing.util.constant.EmailStatus;

@Service
public class EmailTesterService {

    private final EmailTesterRepository emailTesterRepository;
    private final CampaignRepository campaignRepository;
    private final EmailService emailService;

    public EmailTesterService(EmailTesterRepository emailTesterRepository,
            CampaignRepository campaignRepository, EmailService emailService) {
        this.emailTesterRepository = emailTesterRepository;
        this.campaignRepository = campaignRepository;
        this.emailService = emailService;
    }

    private EmailTesterDTO toDTO(EmailTester entity) {
        EmailTesterDTO dto = new EmailTesterDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setStatus(entity.getStatus());
        dto.setSentAt(entity.getSentAt());
        dto.setCreatedAt(entity.getCreatedAt());
        if (entity.getCampaign() != null) {
            dto.setCampaignId(entity.getCampaign().getId());
            dto.setCampaignTitle(entity.getCampaign().getTitle());
        }
        return dto;
    }

    public List<EmailTesterDTO> getAllByCampaign(Long campaignId) {
        return emailTesterRepository.findByCampaignId(campaignId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public EmailTesterDTO create(Long campaignId, EmailTesterDTO dto) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + campaignId));

        if (emailTesterRepository.existsByEmailAndCampaignId(dto.getEmail(), campaignId)) {
            throw new RuntimeException("Email " + dto.getEmail() + " đã tồn tại trong chiến dịch này");
        }

        EmailTester tester = new EmailTester();
        tester.setEmail(dto.getEmail());
        tester.setStatus(Optional.ofNullable(dto.getStatus()).orElse(EmailStatus.PENDING));
        tester.setCampaign(campaign);
        tester.setCreatedAt(Instant.now());

        return toDTO(emailTesterRepository.save(tester));
    }

    @Transactional
    public EmailTesterDTO updateStatus(Long id, EmailStatus newStatus) {
        EmailTester tester = emailTesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmailTester not found with id: " + id));
        tester.setStatus(newStatus);
        if (newStatus == EmailStatus.SENT) {
            tester.setSentAt(Instant.now());
        }
        return toDTO(emailTesterRepository.save(tester));
    }

    public void delete(Long id) {
        emailTesterRepository.deleteById(id);
    }

    @Async
    public List<EmailTesterDTO> sendInvitations(Long campaignId, String customMessage) {
        List<EmailTester> testers = emailTesterRepository.findByCampaignId(campaignId);
        List<EmailTesterDTO> result = new ArrayList<>();

        for (EmailTester tester : testers) {
            if (tester.getStatus() != EmailStatus.PENDING)
                continue; // chỉ gửi mail mới

            try {
                Campaign campaign = tester.getCampaign();
                String campaignTitle = campaign.getTitle();
                // String companyName = campaign.getCompany().getCompanyName();
                String joinLink = "https://betatesting.vn/campaign/" + campaign.getId() + "/join";
                String companyName = "BetaTesting"; // Placeholder for company name

                if (customMessage != null && !customMessage.trim().isEmpty()) {
                    emailService.sendCampaignInvitationEmailCustom(
                            tester.getEmail(),
                            campaignTitle,
                            companyName,
                            joinLink,
                            customMessage);
                } else {
                    emailService.sendCampaignInvitationEmail(
                            tester.getEmail(),
                            campaignTitle,
                            companyName,
                            joinLink);
                }

                tester.setStatus(EmailStatus.SENT);
                tester.setSentAt(Instant.now());
                emailTesterRepository.save(tester);

            } catch (Exception e) {
                tester.setStatus(EmailStatus.FAILED);
                emailTesterRepository.save(tester);
            }

            result.add(toDTO(tester));
        }

        return result;
    }
}