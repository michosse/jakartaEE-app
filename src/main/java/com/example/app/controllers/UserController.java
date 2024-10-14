package com.example.app.controllers;

import com.example.app.DTOs.GetUserResponse;
import com.example.app.DTOs.GetUsersResponse;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.services.UserService;
import jakarta.ws.rs.NotFoundException;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserController {
    private final UserService service;

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
                            .stake(t.getStake())
                            .isWon(t.isWon())
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
