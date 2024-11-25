package com.example.app.controllers;

import com.example.app.DTOs.*;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.GameService;
import com.example.app.services.TicketService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("games")
public class TicketController {
    private TicketService service;
    private GameService gameService;
    private final UriInfo uriInfo;

    @Inject
    public TicketController(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }
    @EJB
    public void setService(TicketService service) {
        this.service = service;
    }
    @EJB
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @GET
    @Path("tickets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTickets() {
        try{
            List<Ticket> tickets = service.findAll();
            GetTicketsResponse response = GetTicketsResponse.builder()
                    .tickets(tickets.stream().map(t -> GetTicketsResponse.Ticket.builder()
                            .id(t.getId())
                            .status(t.isWon())
                            .stake(t.getStake())
                            .build()).collect(Collectors.toList()))
                    .build();
            return Response.ok(response).build();
        } catch (EJBException e){
            return Response.status(401).build();
        }

    }

    @GET
    @Path("{gameid}/tickets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGameTickets(@PathParam("gameid")UUID id){
        try{
            List<Ticket> tickets = gameService.getAllTickets(id);
            GetTicketsResponse response = GetTicketsResponse.builder()
                    .tickets(tickets.stream().map(t -> GetTicketsResponse.Ticket.builder()
                            .id(t.getId())
                            .status(t.isWon())
                            .stake(t.getStake())
                            .build()).collect(Collectors.toList()))
                    .build();
            return Response.ok(response).build();
        } catch (EJBException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{gameid}/tickets/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicket(@PathParam("gameid")UUID gameid, @PathParam("id")UUID id){
        Optional<Ticket> ticket = service.find(id);
        if(ticket.isPresent() && ticket.get().getGame().getId().equals(gameid)){
            GetTicketResponse response = GetTicketResponse.builder()
                    .id(ticket.get().getId())
                    .stake(ticket.get().getStake())
                    .status(ticket.get().isWon())
                    .build();
            return Response.ok(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{gameid}/tickets")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveTicket(@PathParam("gameid")UUID gameid, PostTicketRequest request){
        Optional<Game> game = gameService.find(gameid);
        if(game.isPresent()){
            Ticket ticket = Ticket.builder()
                    .id(UUID.randomUUID())
                    .stake(request.getStake())
                    .isWon(request.isStatus())
                    .user(null)
                    .game(game.get())
                    .build();
            try {
                service.createTicket(ticket);
                return Response.created(uriInfo.getBaseUriBuilder()
                        .path(TicketController.class)
                        .path(TicketController.class, "getTicket")
                        .build(gameid,ticket.getId())).build();
            } catch (EJBException e){
                if(e.getCause() instanceof HttpRequestException){
                    return Response.status(((HttpRequestException) e.getCause()).getResponseCode()).build();
                }
                return Response.status(401).build();
            }
        }
        return Response.status(404,"Game with this id does not exist").build();
    }

    @PUT
    @Path("{gameid}/tickets/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(@PathParam("gameid")UUID gameid, @PathParam("id")UUID id, PutTicketRequest request){
        Optional<Game> game = gameService.find(gameid);
        Optional<Ticket> ticket = service.find(id);
        if(game.isEmpty()){
            return Response.status(404,"Game with this id does not exist").build();
        }
        if(ticket.isEmpty()){
            return Response.status(404,"Ticket with this id does not exist").build();
        }
        ticket.get().setWon(request.isStatus());
        ticket.get().setStake(request.getStake());
        ticket.get().setId(request.getId());
        if(ticket.get().getGame().equals(game.get())){
            ticket.get().setGame(game.get());
        }
        try {
            service.updateTicket(ticket.get());
//        gameService.updateTicket(gameid, ticket.get());
            return Response.ok().build();
        } catch (EJBException e){
            if(e.getCause() instanceof HttpRequestException){
                return Response.status(((HttpRequestException) e.getCause()).getResponseCode()).build();
            }
            return Response.status(401).build();
        }
    }

    @DELETE
    @Path("{gameid}/tickets/{id}")
    public Response deleteTicket(@PathParam("gameid")UUID gameid,@PathParam("id")UUID id){
        Optional<Ticket> ticket =service.find(id);
        if(ticket.isEmpty() || !ticket.get().getGame().getId().equals(gameid)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            service.deleteTicket(id);
            return Response.noContent().build();
        } catch (EJBException e){
            if(e.getCause() instanceof HttpRequestException){
                return Response.status(((HttpRequestException) e.getCause()).getResponseCode()).build();
            }
            return Response.status(401).build();
        }
    }
}
