package com.example.app.DTOs;

import com.example.app.enums.Gender;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PostUserRequest {
    private String name;
    private int age;
    private Gender gender;
    private String password;
}
