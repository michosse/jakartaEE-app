package com.example.app.views;

import com.example.app.DTOs.UpdateTicketRequest;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class TicketCreate implements Serializable {
    private final TicketService ticketService;
    private final GameService gameService;

    @Getter
    private UpdateTicketRequest ticket;

    @Getter
    @Setter
    private UUID selected;

    @Getter
    private List<UUID> games;

    @Inject
    public TicketCreate(TicketService ticketService, GameService gameService) {
        this.ticketService = ticketService;
        this.gameService = gameService;
    }

    public void init(){
        ticket = UpdateTicketRequest.builder()
                .id(UUID.randomUUID())
                .build();
        games = gameService.findAll().stream().map(g->g.getId()).collect(Collectors.toList());
        selected = games.get(0);
    }

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
