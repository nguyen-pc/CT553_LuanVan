package com.vn.beta_testing.feature.register_campaign.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vn.beta_testing.domain.TesterCampaign;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterCampaignDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterCampaignStatsDTO;
import com.vn.beta_testing.feature.register_campaign.repository.TesterCampaignRepository;
import com.vn.beta_testing.util.constant.TesterCampaignStatus;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
public class TesterCampaignService {

    private final TesterCampaignRepository testerCampaignRepository;
    private final CampaignService campaignService;
    private final UserService userService;

    public TesterCampaignService(
            TesterCampaignRepository testerCampaignRepository,
            CampaignService campaignService,
            UserService userService) {
        this.testerCampaignRepository = testerCampaignRepository;
        this.campaignService = campaignService;
        this.userService = userService;
    }

    /** Get all testers by filter */
    public ResultPaginationDTO fetchAll(Specification<TesterCampaign> spec, Pageable pageable) {
        Page<TesterCampaign> page = this.testerCampaignRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;
    }

    /** Get testers by campaign */
    public List<TesterCampaign> getTestersByCampaign(Long campaignId) {
        return testerCampaignRepository.findByCampaign_Id(campaignId);
    }

    public TesterCampaignDTO toDTO(TesterCampaign entity) {
        TesterCampaignDTO dto = new TesterCampaignDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setProgress(entity.getProgress());
        dto.setRoleInCampaign(entity.getRoleInCampaign());
        dto.setJoinDate(entity.getJoinDate());
        dto.setCompletionDate(entity.getCompletionDate());
        dto.setNote(entity.getNote());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getCampaign() != null) {
            dto.setCampaignId(entity.getCampaign().getId());
        }

        return dto;
    }

    /** Apply for campaign */
    public TesterCampaign applyForCampaign(Long userId, Long campaignId, String note) {
        if (testerCampaignRepository.existsByUser_IdAndCampaign_Id(userId, campaignId)) {
            throw new IdInvalidException("User already applied for this campaign.");
        }

        Campaign campaign = campaignService.fetchCampaignById(campaignId);
        if (campaign == null)
            throw new IdInvalidException("Campaign not found.");

        User user = userService.fetchUserById(userId);
        if (user == null)
            throw new IdInvalidException("User not found.");

        TesterCampaign tester = new TesterCampaign();
        tester.setUser(user);
        tester.setCampaign(campaign);
        tester.setStatus(TesterCampaignStatus.PENDING);
        tester.setJoinDate(Instant.now());
        tester.setNote(note);

        return testerCampaignRepository.save(tester);
    }

    /** Approve tester */
    public TesterCampaign approveTester(Long testerCampaignId) {
        TesterCampaign tester = testerCampaignRepository.findById(testerCampaignId)
                .orElseThrow(() -> new IdInvalidException("Tester not found"));
        tester.setStatus(TesterCampaignStatus.APPROVED);
        return testerCampaignRepository.save(tester);
    }

    /** Reject tester */
    public TesterCampaign rejectTester(Long testerCampaignId) {
        TesterCampaign tester = testerCampaignRepository.findById(testerCampaignId)
                .orElseThrow(() -> new IdInvalidException("Tester not found"));
        tester.setStatus(TesterCampaignStatus.REJECTED);
        return testerCampaignRepository.save(tester);
    }

    /** Update progress */
    public TesterCampaign updateProgress(Long testerCampaignId, int progress) {
        TesterCampaign tester = testerCampaignRepository.findById(testerCampaignId)
                .orElseThrow(() -> new IdInvalidException("Tester not found"));

        tester.setProgress(progress);
        if (progress >= 100) {
            tester.setStatus(TesterCampaignStatus.COMPLETED);
            tester.setCompletionDate(Instant.now());
        } else {
            tester.setStatus(TesterCampaignStatus.IN_PROGRESS);
        }

        return testerCampaignRepository.save(tester);
    }

    public TesterCampaignStatsDTO getStatsByCampaign(Long campaignId) {
        TesterCampaignStatsDTO dto = new TesterCampaignStatsDTO();

        dto.setAccepted(testerCampaignRepository.countByCampaignIdAndStatus(campaignId, TesterCampaignStatus.APPROVED));
        dto.setRejected(testerCampaignRepository.countByCampaignIdAndStatus(campaignId, TesterCampaignStatus.REJECTED));
        dto.setPending(testerCampaignRepository.countByCampaignIdAndStatus(campaignId, TesterCampaignStatus.PENDING));
        dto.setApplied(testerCampaignRepository.countByCampaignId(campaignId));
        return dto;
    }
}
