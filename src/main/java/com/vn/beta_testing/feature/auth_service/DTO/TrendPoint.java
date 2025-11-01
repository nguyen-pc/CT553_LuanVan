package com.vn.beta_testing.feature.auth_service.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrendPoint {
    private String date; // yyyy-MM-dd
    private int count;
}