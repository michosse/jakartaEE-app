package com.example.app.services;

import com.example.app.entities.Ticket;
import com.example.app.repositories.TicketRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(force = true)
@RequestScoped
public class TicketService {
    private final TicketRepository ticketRepository;

    @Inject
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    public Optional<Ticket> find(UUID id){
        return ticketRepository.find(id);
    }
    public List<Ticket> findAll(){
        return ticketRepository.findAll();
    }
    public void createTicket(Ticket ticket){
        ticketRepository.create(ticket);
    }
    public void updateTicket(Ticket ticket){
        ticketRepository.update(ticket);
    }
    public void deleteTicket(UUID id){
        ticketRepository.delete(id);
    }


}
