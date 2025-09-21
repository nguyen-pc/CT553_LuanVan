package com.vn.beta_testing.domain;

import com.vn.beta_testing.util.constant.GenderEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int age;

    private String zipcode;

    private String householdIncome;

    private boolean isChildren;

    private String occupation;

    private String education;

    private String gamingGenres;

    private String webBrowser;

    private String webExpertise;

    private String language;

    private String computer;
    private String smartPhone;
    private String tablet;
    private String otherDevice;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

}
