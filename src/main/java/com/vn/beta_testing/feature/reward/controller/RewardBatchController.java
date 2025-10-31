package com.vn.beta_testing.feature.reward.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.RewardBatch;
import com.vn.beta_testing.feature.reward.service.RewardBatchService;
import com.vn.beta_testing.util.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/reward/batch")
public class RewardBatchController {
    private final RewardBatchService batchService;

    public RewardBatchController(RewardBatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping("/create")
    @ApiMessage("Tạo batch + link thanh toán VNPAY")
    public ResponseEntity<RewardBatch> createBatch(@RequestParam("companyId") Long companyId,
                                                   @RequestParam("createdBy") String createdBy,
                                                   HttpServletRequest request)
            throws UnsupportedEncodingException {
        return ResponseEntity.ok(batchService.createBatchAndPayment(
                companyId, createdBy, request.getRemoteAddr()));
    }

    @PostMapping("/vnpay-ipn")
    @ApiMessage("Nhận callback từ VNPAY (IPN)")
    public ResponseEntity<String> vnpayCallback(@RequestParam Map<String, String> params) {
        batchService.handlePaymentCallback(params);
        return ResponseEntity.ok("IPN received");
    }
}