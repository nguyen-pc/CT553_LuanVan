package com.vn.beta_testing.feature.reward.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.UserPaymentInfo;
import com.vn.beta_testing.feature.reward.repository.UserPaymentInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPaymentInfoService {

    private final UserPaymentInfoRepository repository;

    public UserPaymentInfo getByUserId(Long userId) {
        return repository.findById(userId).orElse(null);
    }

    @Transactional
    public UserPaymentInfo updateOrCreate(Long userId, UserPaymentInfo req) {

        UserPaymentInfo info = repository.findById(userId)
                .orElse(UserPaymentInfo.builder()
                        .userId(userId)
                        .build());

        info.setBankName(req.getBankName());
        info.setBankAccountNumber(req.getBankAccountNumber());
        info.setAccountHolder(req.getAccountHolder());

        // reset trạng thái khi user tự sửa
        info.setIsLocked(false);
        info.setVerifiedAt(null);

        return repository.save(info);
    }

    @Transactional
    public UserPaymentInfo adminVerify(Long userId) {
        UserPaymentInfo info = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Payment Info not found"));

        info.setIsLocked(true);
        info.setVerifiedAt(Instant.now());
        return repository.save(info);
    }
}