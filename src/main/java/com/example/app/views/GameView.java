package com.example.app.views;

import com.example.app.DTOs.GetGameResponse;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import com.example.app.views.interceptor.LoggerInterceptor;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
@Log
public class GameView implements Serializable {
    private final GameService gameService;
    private final TicketService ticketService;
    private final SecurityContext securityContext;


    @Setter
    @Getter
    private UUID id;

    @Getter
    private GetGameResponse game;

    @Getter
    @Setter
    private Boolean filterStatus;

    @Getter
    @Setter
    private Double filterStake;

    @Getter
    @Setter
    private List<Boolean> values= new ArrayList<>();

    @Inject
    public GameView(GameService service, TicketService ticketService, SecurityContext securityContext) {
        this.gameService = service;
        this.ticketService = ticketService;
        this.securityContext = securityContext;
        values.add(Boolean.FALSE);
        values.add(Boolean.TRUE);
        values.add(null);
    }

    public void init() throws IOException {
        Optional<Game> game = gameService.find(id);
        List<Ticket> tickets = gameService.getAllTickets(id);
        if(game.isPresent()){
            this.game = GetGameResponse.builder()
                    .id(game.get().getId())
                    .gameDay(game.get().getGameDay())
                    .team1(game.get().getTeam1())
                    .team2(game.get().getTeam2())
                    .tickets(tickets.stream().map(t-> GetGameResponse.Ticket.builder()
                            .id(t.getId())
                            .version(t.getVersion())
                            .lastModify(t.getLastModify())
                            .createdAt(t.getCreatedAt())
                            .build()).collect(Collectors.toList()))
                    .build();
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND,"Game not found");
        }
    }
    @LoggerInterceptor
    public void deleteTicket(UUID id) throws IOException {
        GetGameResponse.Ticket ticket = game.getTickets().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
        if(ticket != null){
            game.getTickets().remove(ticket);
        }
        ticketService.deleteTicket(id);
    }
    @LoggerInterceptor
    public void filter(){
        List<Ticket> tickets = gameService.getAllTickets(id, this.filterStake, this.filterStatus);
        this.game.setTickets(tickets.stream().map(t-> GetGameResponse.Ticket.builder()
                .id(t.getId())
                .version(t.getVersion())
                .createdAt(t.getCreatedAt())
                .lastModify(t.getLastModify())
                .build()).collect(Collectors.toList()));
    }
}
