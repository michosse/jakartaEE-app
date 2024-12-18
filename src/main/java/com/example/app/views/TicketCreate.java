package com.example.app.views;

import com.example.app.DTOs.UpdateTicketRequest;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import com.example.app.views.interceptor.LoggerInterceptor;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
@Log
public class TicketCreate implements Serializable {
    private final TicketService ticketService;
    private final GameService gameService;
    private final SecurityContext securityContext;


    @Getter
    private UpdateTicketRequest ticket;

    @Getter
    @Setter
    private UUID selected;

    @Getter
    private List<UUID> games;

    @Inject
    public TicketCreate(TicketService ticketService, GameService gameService, SecurityContext securityContext) {
        this.ticketService = ticketService;
        this.gameService = gameService;
        this.securityContext = securityContext;
    }

    public void init(){
        ticket = UpdateTicketRequest.builder()
                .id(UUID.randomUUID())
                .build();
        games = gameService.findAll().stream().map(g->g.getId()).collect(Collectors.toList());
        selected = games.get(0);
    }

    @LoggerInterceptor
    public String submit(){
        Optional<Game> game = gameService.find(selected);
        Ticket ticket = Ticket.builder()
                .id(this.ticket.getId())
                .stake(this.ticket.getStake())
                .isWon(false)
                .user(null)
                .game(game.get())
                .build();
        ticketService.createTicket(ticket);
        return "/game/game_list.xhtml?faces-redirect=true";
    }
}
