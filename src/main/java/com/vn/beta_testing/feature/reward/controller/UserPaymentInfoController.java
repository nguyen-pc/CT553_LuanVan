package com.vn.beta_testing.feature.reward.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.UserPaymentInfo;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.reward.DTO.UserPaymentInfoDTO;
import com.vn.beta_testing.feature.reward.service.UserPaymentInfoService;
import com.vn.beta_testing.util.SecurityUtil;
import com.vn.beta_testing.util.annotation.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/payment-info")
@RequiredArgsConstructor
public class UserPaymentInfoController {

    private final UserPaymentInfoService paymentInfoService;
    private final UserService userService;

    @GetMapping
    @ApiMessage("Lấy thông tin thanh toán của chính mình")
    public UserPaymentInfo getMyPaymentInfo() {
        User user = getCurrentUser();
        return paymentInfoService.getByUserId(user.getId());
    }

    @GetMapping("/{userId}")
    @ApiMessage("Lấy thông tin thanh toán của user theo ID")
    public ResponseEntity<?> getPaymentInfoByUserId(@PathVariable("userId") Long userId) {

        UserPaymentInfo info = paymentInfoService.getByUserId(userId);

        if (info == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(info);
    }

    @PostMapping
    @ApiMessage("Cập nhật hoặc tạo mới thông tin thanh toán của chính mình")
    public UserPaymentInfo updateMyPaymentInfo(@RequestBody UserPaymentInfoDTO dto) {
        User user = getCurrentUser();

        UserPaymentInfo req = UserPaymentInfo.builder()
                .bankName(dto.getBankName())
                .bankAccountNumber(dto.getBankAccountNumber())
                .accountHolder(dto.getAccountHolder())
                .build();

        return paymentInfoService.updateOrCreate(user.getId(), req);
    }

    // common
    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Not logged in"));
        return userService.handleGetUserByUsername(email);
    }
}