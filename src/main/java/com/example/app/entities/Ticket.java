package com.example.app.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    private UUID id;
    private double stake;
    private boolean isWon;
    @ManyToOne
    private User user;
    @ManyToOne
    private Game game;
    @Version
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime lastModify;

    @PrePersist
    public void updateCreatedDate() {
        createdAt = LocalDateTime.now();
        lastModify = LocalDateTime.now();
    }
    @PreUpdate
    public void updateModifyDate() {
        lastModify = LocalDateTime.now();
    }
}
