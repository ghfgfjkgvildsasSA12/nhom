package com.example.schoolmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String token;

    private LocalDateTime expiresAt;

    private Boolean revoked;

    private LocalDateTime createdAt;

    private LocalDateTime revokedAt;
}