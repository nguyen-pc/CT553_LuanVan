package com.vn.beta_testing.domain;

import java.time.Instant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_payment_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPaymentInfo {

    @Id
    private Long userId;

    private String bankName;
    private String bankAccountNumber;
    private String accountHolder;

    private Boolean isLocked = false;
    private Instant verifiedAt;
}
