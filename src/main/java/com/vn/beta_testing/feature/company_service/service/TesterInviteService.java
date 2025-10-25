package com.vn.beta_testing.feature.company_service.service;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.TesterInvite;
import com.vn.beta_testing.feature.company_service.repository.CampaignRepository;
import com.vn.beta_testing.feature.company_service.repository.TesterInviteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TesterInviteService {

    private final TesterInviteRepository testerInviteRepository;
    private final CampaignRepository campaignRepository;

    @Transactional
    public List<TesterInvite> saveEmailList(Long campaignId, List<String> emails) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        List<TesterInvite> invites = emails.stream()
                .map(email -> {
                    TesterInvite invite = new TesterInvite();
                    invite.setEmail(email.trim());
                    invite.setStatus(TesterInvite.InviteStatus.PENDING);
                    invite.setInvitedAt(Instant.now());
                    invite.setCampaign(campaign);
                    return invite;
                })
                .toList();

        return testerInviteRepository.saveAll(invites);
    }

    public List<TesterInvite> getInvitesByCampaign(Long campaignId) {
        return testerInviteRepository.findByCampaignId(campaignId);
    }
}