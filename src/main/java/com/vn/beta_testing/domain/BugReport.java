package com.vn.beta_testing.domain;

import java.io.ObjectInputFilter.Status;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.method.P;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.beta_testing.util.SecurityUtil;
import com.vn.beta_testing.util.constant.PriorityEnum;
import com.vn.beta_testing.util.constant.SeverityEnum;
import com.vn.beta_testing.util.constant.StatusBugEnum;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "bug_reports")
public class BugReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private SeverityEnum severity;
    private PriorityEnum priority;
    private StatusBugEnum status;
    private String stepsToReproduce;
    private String expectedResult;
    private String actualResult;
    // private LocalDateTime createdAt;
    // private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "bug_type_id")
    private BugType bugType;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();
    }
}
