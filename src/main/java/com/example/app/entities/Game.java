package com.example.app.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "games")
public class Game {
    @Id
    private UUID id;
    private String team1;
    private String team2;
    private LocalDate gameDay;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}
