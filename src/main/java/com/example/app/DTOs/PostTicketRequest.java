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
public class PostTicketRequest {
    private double stake;
    private boolean status;
}
