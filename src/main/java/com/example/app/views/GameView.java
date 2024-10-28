package com.example.app.views;

import com.example.app.DTOs.GetGameResponse;
import com.example.app.entities.Game;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class GameView implements Serializable {
    private final GameService gameService;
    private final TicketService ticketService;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private GetGameResponse game;

    @Inject
    public GameView(GameService service, TicketService ticketService) {
        this.gameService = service;
        this.ticketService = ticketService;
    }

    public void init() throws IOException {
        Optional<Game> game = gameService.find(id);
        if(game.isPresent()){
            this.game = GetGameResponse.builder()
                    .id(game.get().getId())
                    .gameDay(game.get().getGameDay())
                    .team1(game.get().getTeam1())
                    .team2(game.get().getTeam2())
                    .tickets(game.get().getTickets().stream().map(t-> GetGameResponse.Ticket.builder()
                            .id(t.getId())
                            .build()).collect(Collectors.toList()))
                    .build();
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND,"Game not found");
        }
    }
    public String deleteTicket(UUID id) throws IOException {
        ticketService.deleteTicket(id);
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
