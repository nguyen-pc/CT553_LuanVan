package com.vn.beta_testing.feature.reward.DTO;

import lombok.Data;

@Data
public class UserPaymentInfoDTO {
    private String bankName;
    private String bankAccountNumber;
    private String accountHolder;
}