package com.example.app.DTOs;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetGameResponse {
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Ticket{
        private UUID id;
        private Long version;
        private LocalDateTime createdAt;
        private LocalDateTime lastModify;
    }
    private UUID id;
    private String team1;
    private String team2;
    private LocalDate gameDay;
    private List<Ticket> tickets;
}
