package com.vn.beta_testing.domain;

import java.time.Instant;
import com.vn.beta_testing.util.constant.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reward_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rewardId;

    @Lob
    private String bankSnapshotJson; // lưu JSON thông tin ngân hàng tại thời điểm yêu cầu

    private String transactionCode;
    private String note;
    private String proofUrl;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.WAITING_CONFIRM;

    private Instant createdAt;
    private Instant updatedAt;
}