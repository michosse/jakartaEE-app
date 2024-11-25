package com.example.app.configuration.listeners;

import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.enums.Gender;
import com.example.app.enums.UserRole;
import com.example.app.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@NoArgsConstructor(force = true)
public class InitializeAdminService {

    private final UserRepository userRepository;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public InitializeAdminService(UserRepository userRepository,Pbkdf2PasswordHash passwordHash) {
        this.userRepository = userRepository;
        this.passwordHash = passwordHash;
    }

    @PostConstruct
    @SneakyThrows
    private void init() {
        User admin = User.builder()
                .id(UUID.fromString("1135156e-1111-495c-b2b5-78538145ca5a"))
                .name("admin-service")
                .password(passwordHash.generate("adminadmin".toCharArray()))
                .roles(List.of(UserRole.ADMIN, UserRole.USER))
                .build();

        userRepository.create(admin);
    }
}
