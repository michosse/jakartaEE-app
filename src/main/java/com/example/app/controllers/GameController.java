package com.example.app.controllers;

import com.example.app.DTOs.GetGameResponse;
import com.example.app.DTOs.GetGamesResponse;
import com.example.app.DTOs.PostGameRequest;
import com.example.app.DTOs.PutGameRequest;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.GameService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@NoArgsConstructor(force = true)
@Path("games")
public class GameController {
    private final GameService service;
    private final UriInfo uriInfo;

    @Inject
    public GameController(GameService service, UriInfo uriInfo) {
        this.service = service;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGames(){
        List<Game> games = service.findAll();
        GetGamesResponse response = GetGamesResponse.builder()
                .games(games.stream().map(g -> GetGamesResponse.Game.builder()
                        .id(g.getId())
                        .gameDay(g.getGameDay())
                        .team1(g.getTeam1())
                        .team2(g.getTeam2())
                        .build()).collect(Collectors.toList()))
                .build();
        return Response.ok(response).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGame(@PathParam("id")UUID id){
        Optional<Game> game = service.find(id);
        if(game.isPresent()){
            GetGameResponse response = GetGameResponse.builder()
                    .id(game.get().getId())
                    .gameDay(game.get().getGameDay())
                    .team1(game.get().getTeam1())
                    .team2(game.get().getTeam2())
                    .tickets(game.get().getTickets().stream().map(t->GetGameResponse.Ticket.builder()
                            .id(t.getId())
                            .build()).collect(Collectors.toList()))
                    .build();
            return Response.ok(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteGame(@PathParam("id") UUID id){
        try {
            service.deleteGame(id);
            return Response.noContent().build();
        } catch (HttpRequestException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveGame(PostGameRequest request){
        Game game = Game.builder()
                .id(UUID.randomUUID())
                .gameDay(request.getGameDay())
                .team1(request.getTeam1())
                .team2(request.getTeam2())
                .tickets(new ArrayList<Ticket>())
                .build();
        service.createGame(game);
        return Response.created(uriInfo.getBaseUriBuilder()
                .path(GameController.class)
                .path(GameController.class, "getGame")
                .build(game.getId())).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGame(@PathParam("id") UUID id, PutGameRequest request){
        Optional<Game> game = service.find(id);
        if(game.isPresent()) {
            game.get().setGameDay(request.getGameDay());
            game.get().setTeam2(request.getTeam2());
            game.get().setTeam1(request.getTeam1());
            game.get().setId(request.getId());
            service.updateGame(game.get());
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
