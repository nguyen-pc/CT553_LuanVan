package com.vn.beta_testing.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.beta_testing.util.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_recruit_profiles")
@Getter
@Setter
public class UserRecruitProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Tester Recruiting Section ---
    private String recruitMethod;
    private Integer testerCount;

    @Column(columnDefinition = "TEXT")
    private String devices;

    private String whitelist;

    // --- Audience/Profile Section ---
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String country;
    private String zipcode;
    private String householdIncome;
    private Boolean isChildren;

    @Column(columnDefinition = "TEXT")
    private String employment;

    @Column(columnDefinition = "TEXT")
    private String gamingGenres;

    @Column(columnDefinition = "TEXT")
    private String browsers;

    @Column(columnDefinition = "TEXT")
    private String socialNetworks;

    private String webExpertise;

    @Column(columnDefinition = "TEXT")
    private String languages;

    @Column(columnDefinition = "TEXT")
    private String ownedDevices;

    /** 1–1 relationship with Campaign **/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Campaign campaign;
}
