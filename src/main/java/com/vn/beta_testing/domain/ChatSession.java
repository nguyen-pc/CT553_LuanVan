package com.vn.beta_testing.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "chat_session")
@Getter
@Setter
public class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 64)
    private String sessionUuid;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String topic;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatHistory> histories;
}
