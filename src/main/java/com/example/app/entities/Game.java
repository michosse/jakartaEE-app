package com.example.app.entities;

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
public class Game implements Serializable {
    private UUID id;
    private String team1;
    private String team2;
    private LocalDate gameDay;
    private List<Ticket> tickets;
}
