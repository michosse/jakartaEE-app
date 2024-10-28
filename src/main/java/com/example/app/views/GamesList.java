package com.example.app.views;

import com.example.app.DTOs.GetGamesResponse;
import com.example.app.services.GameService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@Named
public class GamesList {
    private final GameService service;

    private GetGamesResponse games;

    @Inject
    public GamesList(GameService service) {
        this.service = service;
    }


    public GetGamesResponse getGames() {
        if(games == null){
            games = GetGamesResponse.builder()
                    .games(service.findAll().stream().map(g-> GetGamesResponse.Game.builder()
                            .id(g.getId())
                            .gameDay(g.getGameDay())
                            .team1(g.getTeam1())
                            .team2(g.getTeam2())
                            .build()).collect(Collectors.toList()))
                    .build();
        }
        return games;
    }
    public String delete(UUID id){
        service.deleteGame(id);
        return "game_list?faces-redirect=true";
    }
}
