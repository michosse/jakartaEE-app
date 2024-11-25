package com.example.app.controllers;

import com.example.app.DTOs.*;
import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.UserService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@NoArgsConstructor(force = true)
@RequestScoped
@Path("users")
public class UserController {
    private UserService service;
    private final UriInfo uriInfo;

    @Inject
    public UserController(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }
    @EJB
    public void setService(UserService service) {
        this.service = service;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id")UUID id){
        Optional<User> user = service.find(id);
        if(user.isPresent()){
            GetUserResponse response = GetUserResponse.builder()
                    .id(user.get().getId())
                    .age(user.get().getAge())
                    .gender(user.get().getGender())
                    .name(user.get().getName())
                    .tickets(user.get().getTickets().stream().map(t-> GetUserResponse.Ticket.builder()
                            .id(t.getId())
                            .build()).collect(Collectors.toList()))
                    .build();
            return Response.ok(response).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(PostUserRequest request){
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .tickets(new ArrayList<Ticket>())
                .avatar(null)
                .build();
        service.createUser(user);
        return Response.created(uriInfo.getBaseUriBuilder()
                .path(UserController.class)
                .path(UserController.class, "getUser")
                .build(user.getId())).build();
    }
}
