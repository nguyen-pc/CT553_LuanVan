package com.vn.beta_testing.feature.register_campaign.DTO.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletionDailyDTO {
    private LocalDate date;
    private Long completedCount;
}