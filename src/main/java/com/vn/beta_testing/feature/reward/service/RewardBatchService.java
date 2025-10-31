package com.vn.beta_testing.feature.reward.service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.RewardBatch;
import com.vn.beta_testing.domain.TesterReward;
import com.vn.beta_testing.feature.reward.repository.RewardBatchRepository;
import com.vn.beta_testing.feature.reward.repository.TesterRewardRepository;
import com.vn.beta_testing.util.constant.BatchStatus;
import com.vn.beta_testing.util.constant.RewardStatus;

@Service
public class RewardBatchService {
    private final RewardBatchRepository batchRepo;
    private final TesterRewardRepository rewardRepo;
    private final VnPayService vnPayService;

    public RewardBatchService(RewardBatchRepository batchRepo,
            TesterRewardRepository rewardRepo,
            VnPayService vnPayService) {
        this.batchRepo = batchRepo;
        this.rewardRepo = rewardRepo;
        this.vnPayService = vnPayService;
    }

    @Transactional
    public RewardBatch createBatchAndPayment(Long companyId, String createdBy, String clientIp)
            throws UnsupportedEncodingException {
        List<TesterReward> rewards = rewardRepo.findByStatus(RewardStatus.APPROVED);

        if (rewards.isEmpty()) {
            throw new IllegalStateException("Không có tester nào ở trạng thái APPROVED để tạo batch thanh toán!");
        }

        double total = rewards.stream().mapToDouble(TesterReward::getAmount).sum();

        if (total <= 0) {
            throw new IllegalStateException(" Tổng tiền thưởng phải lớn hơn 0!");
        }
        RewardBatch batch = RewardBatch.builder()
                .companyId(companyId)
                .batchCode("BATCH-" + System.currentTimeMillis())
                .totalUsers(rewards.size())
                .totalAmount(total)
                .createdBy(createdBy)
                .status(BatchStatus.PENDING)
                .build();
        batchRepo.save(batch);

        String paymentUrl = vnPayService.createPaymentUrlForBatch(batch, clientIp);
        batch.setPaymentUrl(paymentUrl);
        batchRepo.save(batch);

        rewards.forEach(r -> r.setStatus(RewardStatus.APPROVED));
        rewardRepo.saveAll(rewards);
        return batch;
    }

    @Transactional
    public void handlePaymentCallback(Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef");
        String transactionNo = params.get("vnp_TransactionNo");

        RewardBatch batch = batchRepo.findAll().stream()
                .filter(b -> b.getBatchCode().equals(txnRef))
                .findFirst()
                .orElseThrow();

        batch.setTransactionNo(transactionNo);
        batch.setVnpResponseCode(responseCode);

        if ("00".equals(responseCode)) {
            batch.setStatus(BatchStatus.COMPLETED);
            batch.setPaidAt(Instant.now());
            rewardRepo.findByStatus(RewardStatus.APPROVED)
                    .forEach(r -> {
                        r.setStatus(RewardStatus.TRANSFERRED);
                        r.setTransferredAt(Instant.now());
                    });
        } else {
            batch.setStatus(BatchStatus.FAILED);
        }
        batchRepo.save(batch);
    }
}
