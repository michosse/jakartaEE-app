package com.example.app.controllers;

import com.example.app.DTOs.GetUserResponse;
import com.example.app.DTOs.GetUsersResponse;
import com.example.app.DTOs.PutUserRequest;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@NoArgsConstructor(force = true)
@RequestScoped
public class UserController {
    private final UserService service;

    @Inject
    public UserController(UserService service) {
        this.service = service;
    }

    public GetUserResponse getUser(UUID id){
        Optional<User> user = service.find(id);
        if(user.isPresent()){
            GetUserResponse userDTO = GetUserResponse.builder()
                    .age(user.get().getAge())
                    .gender(user.get().getGender())
                    .id(user.get().getId())
                    .name(user.get().getName())
                    .tickets(user.get().getTickets().stream().map(t -> GetUserResponse.Ticket.builder()
                            .id(t.getId())
                            .build()).collect(Collectors.toList()))
                    .build();
            return userDTO;
        }
        else {
            throw new HttpRequestException(id.toString(),404);
        }
    }

    public GetUsersResponse getUsers(){
        return GetUsersResponse.builder()
                .users(service.findAllUsers().stream().map(u -> GetUsersResponse.User.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public void createUser(PutUserRequest user){
        User userToPut = User.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .tickets(new ArrayList<Ticket>())
                .avatar(null)
                .build();
        service.createUser(userToPut);
    }

    public void updateUser(PutUserRequest user){
        User userToUpdate = User.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .tickets(new ArrayList<Ticket>())
                .avatar(null)
                .build();
        service.updateUser(userToUpdate);
    }

    public void deleteUser(UUID id){
        service.deleteUser(id);
    }

    public byte[] getUsersAvatar(UUID id){
        return service.getUsersAvatar(id);
    }
    public void deleteUsersAvatar(UUID id){
        service.deleteUsersAvatar(id);
    }
    public void putUsersAvatar(UUID id, byte[] avatar){
        service.putUsersAvatar(id, avatar);
    }
}
