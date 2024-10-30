package com.example.app.views;

import com.example.app.DTOs.PutTicketRequest;
import com.example.app.entities.Ticket;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class TicketUpdate implements Serializable {
    private final TicketService ticketService;
    private final GameService gameService;

    @Getter
    @Setter
    private UUID id;

    @Getter
    private PutTicketRequest ticket;

    @Getter
    @Setter
    private UUID selected;

    @Getter
    private List<UUID> games;

    @Inject
    public TicketUpdate(TicketService ticketService, GameService gameService) {
        this.ticketService = ticketService;
        this.gameService = gameService;
    }

    public void init() throws IOException {
        Optional<Ticket> ticket = ticketService.find(this.id);
        if (ticket.isPresent()) {
            this.ticket = PutTicketRequest.builder()
                    .id(ticket.get().getId())
                    .stake(ticket.get().getStake())
                    .status(ticket.get().isWon())
                    .user(ticket.get().getUser())
                    .game(ticket.get().getGame())
                    .build();
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
        games = gameService.findAll().stream().map(g->g.getId()).collect(Collectors.toList());
        selected = games.get(0);
    }

    public String update(){
        Ticket t = Ticket.builder()
                .id(ticket.getId())
                .isWon(ticket.isStatus())
                .stake(ticket.getStake())
                .user(ticket.getUser())
                .game(ticket.getGame())
                .build();
        ticketService.updateTicket(t);
        return "/ticket/ticket_details.xhtml?faces-redirect=true&id="+t.getId().toString();
    }

}
