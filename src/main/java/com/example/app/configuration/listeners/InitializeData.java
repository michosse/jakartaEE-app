package com.example.app.configuration.listeners;

import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.enums.Gender;
import com.example.app.services.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class InitializeData implements ServletContextListener {
    private String avatarsUri;
    private UserService userService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        userService = (UserService) sce.getServletContext().getAttribute("userService");
        avatarsUri = sce.getServletContext().getInitParameter("avatarsPath");
        User u1 = User.builder()
                .id(UUID.fromString("7d35156e-77f5-495c-b2b5-78538145ca5a"))
                .name("test1")
                .age(18)
                .gender(Gender.MALE)
                .tickets(new ArrayList<Ticket>())
                .avatar(getResourceAsByteArray("test1.png"))
                .build();
        User u2 = User.builder()
                .id(UUID.fromString("2d50d429-d5fb-4060-b05b-a70bf84e8949"))
                .name("test2")
                .age(21)
                .gender(Gender.FEMALE)
                .tickets(new ArrayList<Ticket>())
                .avatar(getResourceAsByteArray("test2.png"))
                .build();
        User u3 = User.builder()
                .id(UUID.fromString("7423240b-807b-4f86-a131-35b2ca38f366"))
                .name("test3")
                .age(22)
                .gender(Gender.MALE)
                .tickets(new ArrayList<Ticket>())
                .avatar(getResourceAsByteArray("test3.png"))
                .build();
        User u4 = User.builder()
                .id(UUID.fromString("88263c0a-9eca-4dec-b994-6b737d3e6509"))
                .name("test4")
                .age(24)
                .gender(Gender.MALE)
                .tickets(new ArrayList<Ticket>())
                .avatar(getResourceAsByteArray("test4.png"))
                .build();
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
    }

    private byte[] getResourceAsByteArray(String fileName) {
        String mergedString = avatarsUri.concat(fileName);
        Path path = Paths.get(mergedString);
        try{
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to get resource %s".formatted(path));
        }
    }
}
