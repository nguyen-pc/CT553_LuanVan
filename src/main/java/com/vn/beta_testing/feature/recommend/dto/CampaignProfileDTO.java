package com.vn.beta_testing.feature.recommend.dto;

import lombok.Data;

@Data
public class CampaignProfileDTO {
    private Long id;
    private Long campaignId;
    private String gender;
    private String country;
    private String zipcode;
    private String householdIncome;
    private Boolean isChildren;
    private String employment;
    private String gamingGenres;
    private String browsers;
    private String languages;
    private String ownedDevices;
    private String devices;
}
