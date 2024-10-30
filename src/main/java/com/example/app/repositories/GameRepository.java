package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@ApplicationScoped
public class GameRepository {
    private final Set<Game> games = new HashSet<>();
    private final TicketRepository ticketRepository;
    @Inject
    public GameRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Optional<Game> find(UUID id){
        return games.stream().filter(g->g.getId().equals(id)).findFirst();
    }
    public List<Game> findAll(){
        return games.stream().collect(Collectors.toList());
    }
    public void create(Game game){
        games.add(game);
    }
    public void delete(UUID id){
        Optional<Game> game = this.find(id);
        if(game.isPresent()){
            List<UUID> ticketIds = game.get().getTickets().stream()
                    .map(g -> g.getId())
                    .collect(Collectors.toList());
            ticketIds.forEach(i -> ticketRepository.delete(i));
            games.remove(game.get());
        }
        else {
            throw new HttpRequestException(404);
        }
    }
    public void update(Game game){
        Optional<Game> gameToChange = games.stream().filter(g->g.getId().equals(game.getId())).findFirst();
        if(gameToChange.isPresent()){
            gameToChange.get().setGameDay(game.getGameDay());
            gameToChange.get().setTeam1(game.getTeam1());
            gameToChange.get().setTeam2(game.getTeam2());
        }
        else {
            throw new HttpRequestException("Game with this id does not exist", 404);
        }
    }
    public void addTicket(Ticket ticket){
        Optional<Game> game = games.stream().filter(g->g.getId().equals(ticket.getGame().getId())).findFirst();
        if(game.isPresent()){
            game.get().getTickets().add(ticket);
        }
        else {
            throw new HttpRequestException("Game with this id does not exist", 404);
        }
    }
    public void deleteTicket(UUID ticketId, UUID gameId){
        Optional<Game> game = this.find(gameId);
        if(game.isPresent()){
            Optional<Ticket> ticket = game.get().getTickets().stream().filter(t->t.getId().equals(ticketId)).findFirst();
            ticket.ifPresent(value -> game.get().getTickets().remove(value));
        }
        else {
            throw new HttpRequestException(404);
        }
    }
}
