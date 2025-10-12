package com.vn.beta_testing.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vn.beta_testing.util.constant.GenderEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private String birthYear;
    private String zipcode;
    private String country;
    private String householdIncome;

    private boolean isChildren;

    private String employment;

    private String education;

    private String gamingGenres;

    private String browsers;

    private String webExpertise;

    private String language;

    private String computer;
    private String smartPhone;
    private String tablet;
    private String otherDevice;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    /** 1–1 relationship with User **/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User user;
}
