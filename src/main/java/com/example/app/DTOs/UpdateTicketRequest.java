package com.example.app.DTOs;

import com.example.app.entities.Game;
import com.example.app.entities.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateTicketRequest {
    private UUID id;
    private double stake;
    private boolean status;
    private User user;
    private Game game;
    private Long version;
}
