package com.vn.beta_testing.feature.recommend.dto;


import lombok.Data;

@Data
public class UserAudienceDTO {
    private Long id;
    private String country;
    private String zipcode;
    private String gender;
    private String languages;
    private String browsers;
    private String computer;
    private String smartPhone;
    private String otherDevice;
    private String employment;
    private String gamingGenres;
    private String householdIncome;
    private Boolean children;
}
