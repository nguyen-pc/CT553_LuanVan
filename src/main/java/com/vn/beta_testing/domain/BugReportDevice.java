package com.vn.beta_testing.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "bug_report_device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BugReportDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String device; 
    private String os; 
    private String browser;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    private Long bugId;      
}