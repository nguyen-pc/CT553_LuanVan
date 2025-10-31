package com.vn.beta_testing.feature.company_service.DTO;

import com.vn.beta_testing.util.constant.GenderEnum;
import lombok.Data;

@Data
public class UserRecruitProfileDTO {
    private Long id;

    // tester recruiting
    private String recruitMethod;
    private Integer testerCount;
    private String devices;
    private String whitelist;

    // audience
    private GenderEnum gender;
    private String country;
    private String zipcode;
    private String householdIncome;
    private Boolean isChildren;
    private String employment;
    private String gamingGenres;
    private String browsers;
    private String socialNetworks;
    private String webExpertise;
    private String languages;
    private String ownedDevices;

    // chỉ lấy id campaign
    private Long campaignId;
}
