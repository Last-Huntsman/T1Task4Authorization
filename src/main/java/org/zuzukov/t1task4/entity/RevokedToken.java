package org.zuzukov.t1task4.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "revoked_tokens")
@Data
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "revoked_at", nullable = false)
    private LocalDateTime revokedAt;
}
