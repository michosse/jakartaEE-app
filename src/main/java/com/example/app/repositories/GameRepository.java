package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@ApplicationScoped
public class GameRepository {
    private final Set<Game> games = new HashSet<>();

    public Optional<Game> find(UUID id){
        return games.stream().filter(g->g.getId().equals(id)).findFirst();
    }
    public List<Game> findAll(){
        return games.stream().collect(Collectors.toList());
    }
    public void create(Game game){
        games.add(game);
    }
    public void delete(Game game){
        games.remove(game);
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
}
