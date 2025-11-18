package com.vn.beta_testing.feature.reward.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.RewardBatch;
import com.vn.beta_testing.domain.RewardBatchEvidence;
import com.vn.beta_testing.domain.TesterReward;
import com.vn.beta_testing.feature.reward.DTO.AddTesterRewardRequest;
import com.vn.beta_testing.feature.reward.DTO.CreateRewardBatchRequest;
import com.vn.beta_testing.feature.reward.DTO.UpdateTesterRewardStatusRequest;
import com.vn.beta_testing.feature.reward.DTO.UploadEvidenceRequest;
import com.vn.beta_testing.feature.reward.repository.RewardBatchEvidenceRepository;
import com.vn.beta_testing.feature.reward.repository.RewardBatchRepository;
import com.vn.beta_testing.feature.reward.repository.TesterRewardRepository;
import com.vn.beta_testing.util.constant.RewardBatchStatus;
import com.vn.beta_testing.util.constant.TesterRewardStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardBatchService {

    private final RewardBatchRepository rewardBatchRepository;
    private final TesterRewardRepository testerRewardRepository;
    private final RewardBatchEvidenceRepository evidenceRepository;

    /** Create Reward Batch */
    @Transactional
    public RewardBatch createBatch(CreateRewardBatchRequest req, Long companyId) {
        RewardBatch batch = new RewardBatch();
        batch.setCampaignId(req.getCampaignId());
        batch.setCompanyId(companyId);
        batch.setStatus(RewardBatchStatus.DRAFT);
        batch.setNote(req.getNote());

        return rewardBatchRepository.save(batch);
    }

    public List<RewardBatch> getByCampaignId(Long campaignId) {
        return rewardBatchRepository.findByCampaignIdOrderByCreatedAtDesc(campaignId);
    }

    /** Approve batch */
    @Transactional
    public RewardBatch approveBatch(Long batchId, Long approverId) {
        RewardBatch batch = rewardBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("RewardBatch not found"));

        List<TesterReward> rewards = testerRewardRepository.findByRewardBatchId(batchId);

        BigDecimal totalAmount = rewards.stream()
                .map(TesterReward::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        batch.setTotalAmount(totalAmount);
        batch.setApprovedById(approverId);
        batch.setApprovedAt(LocalDateTime.now());
        batch.setStatus(RewardBatchStatus.APPROVED);

        return rewardBatchRepository.save(batch);
    }

    /** Update tester reward status */

    public TesterReward addTesterReward(AddTesterRewardRequest req) {

        RewardBatch batch = rewardBatchRepository.findById(req.getRewardBatchId())
                .orElseThrow(() -> new RuntimeException("Reward batch not found"));

        TesterReward reward = new TesterReward();
        reward.setRewardBatchId(req.getRewardBatchId());
        reward.setTesterId(req.getTesterId());

        reward.setAmount(req.getRewardAmount());

        reward.setBankAccountNumber(req.getBankAccountNumber());
        reward.setBankAccountName(req.getBankAccountName());
        reward.setBankName(req.getBankName());

        reward.setStatus(TesterRewardStatus.PENDING);

        return testerRewardRepository.save(reward);
    }

    public List<TesterReward> getRewardsByBatchId(Long batchId) {
        return testerRewardRepository.findByRewardBatchId(batchId);
    }

    @Transactional
    public TesterReward updateTesterReward(Long rewardId, UpdateTesterRewardStatusRequest req) {
        TesterReward reward = testerRewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Tester reward not found"));

        reward.setStatus(req.getStatus());
        reward.setFailureReason(req.getFailureReason());
        reward.setEvidenceUrl(req.getEvidenceUrl());

        if (req.getStatus() == TesterRewardStatus.PAID) {
            reward.setPaidAt(LocalDateTime.now());
        }

        return testerRewardRepository.save(reward);
    }

    /** Upload batch evidence */
    @Transactional
    public RewardBatchEvidence uploadEvidence(Long batchId, UploadEvidenceRequest req, Long actorId) {

        RewardBatch batch = rewardBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("RewardBatch not found"));

        RewardBatchEvidence ev = new RewardBatchEvidence();
        ev.setRewardBatchId(batch.getId());
        ev.setType(req.getType());
        ev.setFileName(req.getFileName());
        ev.setFileUrl(req.getFileUrl());
        ev.setNote(req.getNote());
        ev.setUploadedById(actorId);

        return evidenceRepository.save(ev);
    }

    /** Tester history */
    public List<TesterReward> getTesterHistory(Long testerId) {
        return testerRewardRepository.findByTesterId(testerId);
    }

    @Transactional
    public RewardBatch finalizeBatch(Long batchId, Long adminId) {

        RewardBatch batch = rewardBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Reward batch not found"));

        if (batch.getStatus() != RewardBatchStatus.APPROVED) {
            throw new RuntimeException("Batch must be APPROVED before finalizing");
        }

        // check tất cả testerReward phải PAID hoặc FAILED
        List<TesterReward> rewards = testerRewardRepository.findByRewardBatchId(batchId);

        boolean allHandled = rewards.stream()
                .allMatch(r -> r.getStatus() == TesterRewardStatus.PAID ||
                        r.getStatus() == TesterRewardStatus.FAILED);

        if (!allHandled) {
            throw new RuntimeException("Not all tester rewards are settled (must be PAID or FAILED)");
        }

        // Update trạng thái batch
        batch.setStatus(RewardBatchStatus.COMPLETED);
        batch.setFinalizedById(adminId);
        batch.setFinalizedAt(LocalDateTime.now());

        return rewardBatchRepository.save(batch);
    }

    public RewardBatchEvidence getEvidenceByBatchId(Long batchId) {
        List<RewardBatchEvidence> evidences = evidenceRepository.findByRewardBatchId(batchId);
        return evidences.isEmpty() ? null : evidences.get(0);
    }

    public List<RewardBatch> getAllBatchesForAdmin() {
        return rewardBatchRepository.findAll();
    }

}
