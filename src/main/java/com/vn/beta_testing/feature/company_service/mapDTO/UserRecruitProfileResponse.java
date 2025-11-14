package com.vn.beta_testing.feature.company_service.mapDTO;

import com.vn.beta_testing.util.constant.GenderEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRecruitProfileResponse {
    private Long id;
    private String recruitMethod;
    private Integer testerCount;
    private String devices;
    private String whitelist;
    @Enumerated(EnumType.STRING)
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
    private Long campaignId; // chỉ trả ID
}
