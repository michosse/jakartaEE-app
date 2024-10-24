package com.example.app.repositories;

import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@ApplicationScoped
public class TicketRepository {
    private final Set<Ticket> tickets = new HashSet<>();
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Inject
    public TicketRepository(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Optional<Ticket> find(UUID id){
        return tickets.stream().filter(t->t.getId().equals(id)).findFirst();
    }
    public List<Ticket> findAll() {
        return tickets.stream().collect(Collectors.toList());
    }
    public void create(Ticket ticket){
        tickets.add(ticket);
        gameRepository.addTicket(clone(ticket));
        userRepository.addTicket(clone(ticket));
    }
    public void delete(Ticket ticket){
        tickets.remove(ticket);
    }
    public void update(Ticket ticket){
        Optional<Ticket> ticketToChange = tickets.stream().filter(t->t.getId().equals(ticket.getId())).findFirst();
        if(ticketToChange.isPresent()){
            ticketToChange.get().setWon(ticket.isWon());
            ticketToChange.get().setStake(ticket.getStake());
        }
        else {
            throw new HttpRequestException("Ticket with this id does not exist", 404);
        }
    }

    @SneakyThrows
    private Ticket clone(Ticket ticket){
        try(ByteArrayInputStream is = new ByteArrayInputStream(writeObject(ticket).toByteArray());
            ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Ticket) ois.readObject();
        }
    }
    private ByteArrayOutputStream writeObject(Ticket object) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(object);
            return os;
        }
    }
}
