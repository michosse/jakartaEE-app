package com.example.app.DTOs;

import com.example.app.entities.Ticket;
import com.example.app.enums.Gender;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PutUserRequest {
    private UUID  id;
    private String name;
    private int age;
    private Gender gender;
}
