package com.example.app.configuration.listeners;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.enums.Gender;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import com.example.app.services.UserService;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
@ApplicationScoped
public class InitializeData{
    @Resource(name = "avatarsPath")
    private String avatarsUri;
    private final UserService userService;
    private final GameService gameService;
    private final TicketService ticketService;
    private final RequestContextController requestContextController;

    @Inject
    public InitializeData(UserService userService, GameService gameService, TicketService ticketService, RequestContextController requestContextController) {
        this.userService = userService;
        this.gameService = gameService;
        this.ticketService = ticketService;
        this.requestContextController = requestContextController;
    }
    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
     init();
    }
    private void init(){
        requestContextController.activate();
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
        Game g1 = Game.builder()
                .id(UUID.randomUUID())
                .tickets(new ArrayList<>())
                .team1("Drużyna11")
                .team2("Drużyna12")
                .gameDay(LocalDate.now())
                .build();
        Game g2 = Game.builder()
                .id(UUID.randomUUID())
                .tickets(new ArrayList<>())
                .team1("Drużyna21")
                .team2("Drużyna22")
                .gameDay(LocalDate.now())
                .build();
        Ticket t1 = Ticket.builder()
                .id(UUID.randomUUID())
                .user(u1)
                .stake(2.2)
                .isWon(false)
                .game(g1)
                .build();
        Ticket t2 = Ticket.builder()
                .id(UUID.randomUUID())
                .user(u2)
                .stake(2.3)
                .isWon(false)
                .game(g2)
                .build();
        Ticket t3 = Ticket.builder()
                .id(UUID.randomUUID())
                .user(u3)
                .stake(2.2)
                .isWon(false)
                .game(g1)
                .build();
        Ticket t4 = Ticket.builder()
                .id(UUID.randomUUID())
                .user(u4)
                .stake(2.3)
                .isWon(false)
                .game(g2)
                .build();
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        gameService.createGame(g1);
        gameService.createGame(g2);
        ticketService.createTicket(t1);
        ticketService.createTicket(t2);
        ticketService.createTicket(t3);
        ticketService.createTicket(t4);
        requestContextController.deactivate();
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
