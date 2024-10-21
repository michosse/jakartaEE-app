package com.example.app.entities;

import com.example.app.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {
    private UUID id;
    private String name;
    private int age;
    private Gender gender;
    private List<Ticket> tickets;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] avatar;
}
