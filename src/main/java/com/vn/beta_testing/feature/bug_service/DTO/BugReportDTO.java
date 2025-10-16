package com.vn.beta_testing.feature.bug_service.DTO;

import com.vn.beta_testing.util.constant.PriorityEnum;
import com.vn.beta_testing.util.constant.SeverityEnum;
import com.vn.beta_testing.util.constant.StatusBugEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BugReportDTO {
    private Long id;
    private String title;
    private String description;
    private SeverityEnum severity;
    private PriorityEnum priority;
    private StatusBugEnum status;
    private String stepsToReproduce;
    private String expectedResult;
    private String actualResult;

    private Long testerId;
    private Long assigneeId;
    private Long bugTypeId;
    private Long campaignId;
}