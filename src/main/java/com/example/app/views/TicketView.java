package com.example.app.views;

import com.example.app.DTOs.GetGameResponse;
import com.example.app.DTOs.GetTicketResponse;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.TicketService;
import jakarta.ejb.EJBException;
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
public class TicketView implements Serializable {
    private final TicketService ticketService;
    @Setter
    @Getter
    private UUID id;

    @Getter
    private GetTicketResponse ticket;

    @Inject
    public TicketView(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void init() throws IOException {
        try{
            Optional<Ticket> ticket = ticketService.find(id);
            if(ticket.isPresent()){
                this.ticket = GetTicketResponse.builder()
                        .id(ticket.get().getId())
                        .stake(ticket.get().getStake())
                        .status(ticket.get().isWon())
                        .build();
            }
            else {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND,"Ticket not found");
            }
        } catch (EJBException e){
            if(e.getCause() instanceof HttpRequestException){
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(((HttpRequestException) e.getCause()).getResponseCode(),"Ticket not found");
            }
        }
    }
}
