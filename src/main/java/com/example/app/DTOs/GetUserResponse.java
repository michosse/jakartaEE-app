package com.example.app.DTOs;

import com.example.app.enums.Gender;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Ticket{
        private UUID id;
        private double stake;
        private boolean isWon;
    }
    private UUID id;
    private String name;
    private int age;
    private Gender gender;
    private List<Ticket> tickets;
}
