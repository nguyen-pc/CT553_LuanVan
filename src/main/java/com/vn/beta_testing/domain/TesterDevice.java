package com.vn.beta_testing.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tester_devices")
@Getter
@Setter
public class TesterDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceModel;
    private String osName;
    private String osVersion;
    private String browserName;
    private String browserVersion;
    private String appVersion;
    private String buildInfo;
}