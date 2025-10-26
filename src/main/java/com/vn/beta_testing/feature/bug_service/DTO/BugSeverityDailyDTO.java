package com.vn.beta_testing.feature.bug_service.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugSeverityDailyDTO {
    private LocalDate date;
    private String severity;
    private Long count;
}