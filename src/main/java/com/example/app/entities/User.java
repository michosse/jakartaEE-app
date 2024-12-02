package com.example.app.entities;

import com.example.app.enums.Gender;
import com.example.app.enums.UserRole;
import jakarta.persistence.*;
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
@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private String password;
    private String name;
    private int age;
    private Gender gender;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Ticket> tickets;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] avatar;
    @CollectionTable(name = "users__roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
}
