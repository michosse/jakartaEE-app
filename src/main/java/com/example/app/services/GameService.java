package com.example.app.services;

import com.example.app.entities.Game;
import com.example.app.repositories.GameRepository;
import com.example.app.repositories.TicketRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(force = true)
@RequestScoped
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
    public void createGame(Game game){
        gameRepository.create(game);
    }
    public void updateGame(Game game){
        gameRepository.update(game);
    }
    public void deleteGame(UUID id){
        gameRepository.delete(id);
    }
}
