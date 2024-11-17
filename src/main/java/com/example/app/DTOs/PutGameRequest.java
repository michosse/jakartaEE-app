package com.example.app.DTOs;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PutGameRequest {
    private UUID id;
    private String team1;
    private String team2;
    private LocalDate gameDay;
}
