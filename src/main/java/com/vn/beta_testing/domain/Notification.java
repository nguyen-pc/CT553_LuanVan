package com.vn.beta_testing.domain;

import java.time.Instant;

import com.vn.beta_testing.util.constant.NotificationEnum;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private NotificationEnum type;
    private String title;
    private String content;

    private boolean isRead = false;
 
    private Instant createdAt;
}
