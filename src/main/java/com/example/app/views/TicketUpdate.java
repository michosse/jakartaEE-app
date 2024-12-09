package com.example.app.views;

import com.example.app.DTOs.UpdateTicketRequest;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import com.example.app.views.interceptor.LoggerInterceptor;
import jakarta.ejb.EJBException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
@Log
public class TicketUpdate implements Serializable {
    private final TicketService ticketService;
    private final GameService gameService;
    private final SecurityContext securityContext;
    private final FacesContext facesContext;


    @Getter
    @Setter
    private UUID id;

    @Getter
    private UpdateTicketRequest ticket;
    private LocalDateTime createdAt;

    @Getter
    @Setter
    private UUID selected;

    @Getter
    private List<UUID> games;

    @Inject
    public TicketUpdate(TicketService ticketService, GameService gameService, SecurityContext securityContext, FacesContext facesContext) {
        this.ticketService = ticketService;
        this.gameService = gameService;
        this.securityContext = securityContext;
        this.facesContext = facesContext;
    }

    public void init() throws IOException {
        try{
            Optional<Ticket> ticket = ticketService.find(this.id);
            if (ticket.isPresent()) {
                this.ticket = UpdateTicketRequest.builder()
                        .id(ticket.get().getId())
                        .stake(ticket.get().getStake())
                        .status(ticket.get().isWon())
                        .user(ticket.get().getUser())
                        .game(ticket.get().getGame())
                        .version(ticket.get().getVersion())
                        .build();
                createdAt = ticket.get().getCreatedAt();
            } else {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
            }
            games = gameService.findAll().stream().map(g->g.getId()).collect(Collectors.toList());
            selected = games.get(0);
        } catch (EJBException e){
            if(e.getCause() instanceof HttpRequestException){
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(((HttpRequestException) e.getCause()).getResponseCode(),"Ticket not found");
            }
        }
    }

    @LoggerInterceptor
    public String update() throws IOException {
        try{
            Ticket t = Ticket.builder()
                    .id(ticket.getId())
                    .isWon(ticket.isStatus())
                    .stake(ticket.getStake())
                    .user(ticket.getUser())
                    .game(ticket.getGame())
                    .version(ticket.getVersion())
                    .createdAt(createdAt)
                    .build();
            ticketService.updateTicket(t);
            return "/ticket/ticket_details.xhtml?faces-redirect=true&id="+t.getId().toString();
        } catch (EJBException e){
            if(e.getCause() instanceof OptimisticLockException){
                UUID id = ticket.getId();
                double stake = ticket.getStake();
                boolean status = ticket.isStatus();
                init();
                ticket.setId(id);
                ticket.setStake(stake);
                ticket.setStatus(status);
                facesContext.addMessage(null, new FacesMessage("Version collision."));
            }
            return null;
        }

    }

}
