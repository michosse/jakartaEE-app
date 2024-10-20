package com.example.app.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Ticket implements Serializable {
    private UUID id;
    private double stake;
    private boolean isWon;
    private User user;
    private Game game;
}
