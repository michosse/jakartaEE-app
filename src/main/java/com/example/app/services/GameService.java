package com.example.app.services;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.repositories.GameRepository;
import com.example.app.repositories.TicketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class GameService {
    private final GameRepository gameRepository;
    private final TicketRepository ticketRepository;

    @Inject
    public GameService(GameRepository gameRepository, TicketRepository ticketRepository) {
        this.gameRepository = gameRepository;
        this.ticketRepository = ticketRepository;
    }

    public Optional<Game> find(UUID id){
        return gameRepository.find(id);
    }
    public List<Game> findAll(){
        return gameRepository.findAll();
    }
    public List<Ticket> getAllTickets(UUID id){
        return gameRepository.findAllTickets(id);
    }
    @Transactional
    public void createGame(Game game){
        gameRepository.create(game);
    }
    @Transactional
    public void updateGame(Game game){
        gameRepository.update(game);
    }
    @Transactional
    public void deleteGame(UUID id){
        gameRepository.delete(id);
    }
//    public void updateTicket(UUID gameId, Ticket ticket){
//        gameRepository.updateTicket(gameId, ticket);
//    }
}
