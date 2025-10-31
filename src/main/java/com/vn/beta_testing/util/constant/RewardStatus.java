package com.vn.beta_testing.util.constant;

public enum RewardStatus {
  PENDING,          // Hệ thống tự động đánh dấu khi progress = 100%
  REQUESTED,        // Tester yêu cầu nhận thưởng
  VERIFIED,         // Admin xác minh yêu cầu hợp lệ (thông tin đúng, không gian lận)
  APPROVED,         // Company xác nhận sẽ chi trả
  TRANSFERRED,      // Company upload chứng từ chuyển tiền
  CONFIRMED,        // Admin duyệt xác nhận cuối cùng (tiền đã nhận)
  REJECTED          // Bị từ chối ở bất kỳ bước nào
}