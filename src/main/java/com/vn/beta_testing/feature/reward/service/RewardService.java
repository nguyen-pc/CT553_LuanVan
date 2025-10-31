package com.vn.beta_testing.feature.reward.service;

import java.time.Instant;
import java.util.*;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.TesterReward;
import com.vn.beta_testing.feature.reward.DTO.*;
import com.vn.beta_testing.feature.reward.repository.TesterRewardRepository;
import com.vn.beta_testing.util.constant.RewardStatus;

@Service
public class RewardService {

    private final TesterRewardRepository rewardRepo;

    public RewardService(TesterRewardRepository rewardRepo) {
        this.rewardRepo = rewardRepo;
    }

    // 1️⃣ Tester gửi yêu cầu nhận thưởng
    // @Transactional
    public TesterReward requestReward(Long userId, RewardRequestDTO dto) {
        TesterReward reward = TesterReward.builder()
                .userId(userId)
                .testerCampaignId(dto.getTesterCampaignId())
                .amount(dto.getAmount())
                .status(RewardStatus.REQUESTED)
                .requestedAt(Instant.now())
                .build();
        return rewardRepo.save(reward);
    }

    // 2️⃣ Admin xác minh yêu cầu
    // @Transactional
    public TesterReward verifyReward(Long rewardId, String adminName) {
        TesterReward reward = rewardRepo.findById(rewardId).orElseThrow();
        reward.setStatus(RewardStatus.VERIFIED);
        reward.setVerifiedBy(adminName);
        reward.setVerifiedAt(Instant.now());
        return rewardRepo.save(reward);
    }

    // 3️⃣ Company duyệt và chuyển tiền
    // @Transactional
    public TesterReward approveReward(Long rewardId, String companyName, String proofUrl) {
        TesterReward reward = rewardRepo.findById(rewardId).orElseThrow();
        reward.setStatus(RewardStatus.TRANSFERRED);
        reward.setApprovedBy(companyName);
        reward.setApprovedAt(Instant.now());
        reward.setProofUrl(proofUrl);
        reward.setTransferredAt(Instant.now());
        return rewardRepo.save(reward);
    }

    // 4️⃣ Admin xác nhận hoàn tất
    // @Transactional
    public TesterReward confirmReward(Long rewardId, String adminName) {
        TesterReward reward = rewardRepo.findById(rewardId).orElseThrow();
        reward.setStatus(RewardStatus.CONFIRMED);
        reward.setConfirmedBy(adminName);
        reward.setConfirmedAt(Instant.now());
        return rewardRepo.save(reward);
    }

    // 5️⃣ Lấy Dashboard Analytics
    public RewardAnalyticsDTO getRewardAnalytics() {
        Double total = (double) rewardRepo.countAllRewards();
        Double approved = (double) rewardRepo.countApproved();
        Double rejected = (double) rewardRepo.countRejected();

        Double approvalRate = total == 0 ? 0 : (approved / total) * 100;
        Double rejectionRate = total == 0 ? 0 : (rejected / total) * 100;

        Double avgVerify = Optional.ofNullable(rewardRepo.avgVerificationTime()).orElse(0.0);
        Double avgTransfer = Optional.ofNullable(rewardRepo.avgTransferTime()).orElse(0.0);
        Double totalReward = Optional.ofNullable(rewardRepo.sumRewardCurrentMonth(Instant.now())).orElse(0.0);

        Map<String, Double> rewardByMonth = new LinkedHashMap<>();
        for (Object[] row : rewardRepo.sumRewardByMonth()) {
            rewardByMonth.put((String) row[0], (Double) row[1]);
        }

        return RewardAnalyticsDTO.builder()
                .approvalRate(approvalRate)
                .rejectionRate(rejectionRate)
                .avgVerificationTime(avgVerify)
                .avgTransferTime(avgTransfer)
                .totalRewardThisMonth(totalReward)
                .activeTesters(approved.longValue())
                .rewardByMonth(rewardByMonth)
                .build();
    }
}
