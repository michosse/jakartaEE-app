package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@ApplicationScoped
public class UserRepository {
    private final Set<User> users = new HashSet<>();
    private final TicketRepository ticketRepository;
    @Resource(name = "avatarsPath")
    private String avatarsUri;
    @Inject
    public UserRepository(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    public Optional<User> find(UUID id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
    public List<User> findAll() {
        return users.stream().collect(Collectors.toList());
    }
    public void create(User user){
        users.add(user);
    }
    public void delete(UUID id){
        Optional<User> user = this.find(id);
        if(user.isPresent()){
            for (Ticket t: user.get().getTickets()) {
                ticketRepository.delete(t.getId());
            }
            users.remove(user.get());
        }
        else {
            throw new HttpRequestException(404);
        }
    }
    public void update(User user){
        Optional<User> userToChange = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst();
        if(userToChange.isPresent()){
            userToChange.get().setAge(user.getAge());
            userToChange.get().setName(user.getName());
            userToChange.get().setGender(user.getGender());
        }

    }
    public void addTicket(Ticket ticket){
        Optional<User> user = users.stream().filter(u->u.getId().equals(ticket.getUser().getId())).findFirst();
        if(user.isPresent()){
            user.get().getTickets().add(ticket);
        }
        else {
            throw new HttpRequestException("User with this id does not exist", 404);
        }
    }

    public void deleteTicket(UUID ticketId, UUID userId){
        Optional<User> user = this.find(userId);
        if(user.isPresent()){
            Optional<Ticket> ticket = user.get().getTickets().stream().filter(t->t.getId().equals(ticketId)).findFirst();
            ticket.ifPresent(value -> user.get().getTickets().remove(value));
        }
        else {
            throw new HttpRequestException(404);
        }
    }

    public void putAvatar(UUID id, byte[] avatar) {
        Optional<User> userToChange = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        if(userToChange.isPresent()){
            try{
                userToChange.get().setAvatar(avatar);
                saveBytesToFile(userToChange.get().getAvatar(), userToChange.get().getName());
            } catch (IllegalStateException e){
                throw new HttpRequestException("Cannot save file",400);
            }
        }else {
            throw new HttpRequestException("User not found", 404);
        }
    }
    public byte[] getAvatar(UUID id){
        Optional<User> userToChange = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        if(userToChange.isPresent() && userToChange.get().getAvatar() != null){
            return userToChange.get().getAvatar();
        }else {
            throw new HttpRequestException("Avatar not found",404);
        }
    }
    public void deleteAvatar(UUID id){
        Optional<User> userToChange = users.stream().filter(u->u.getId().equals(id)).findFirst();
        if(userToChange.isPresent()){
            userToChange.get().setAvatar(null);
            try{
            deleteAvatarFile(userToChange.get().getName());
            } catch (IllegalStateException e){
                throw new HttpRequestException("Avatar not found", 404);
            }
        }else {
            throw new HttpRequestException("User not found",404);
        }
    }

    private void saveBytesToFile(byte[] avatar, String fileName) {
        String mergedString = avatarsUri.concat(fileName).concat(".png");
        Path path = Paths.get(mergedString);
        try{
            Files.write(path,avatar, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to get resource %s".formatted(path));
        }
    }
    private void deleteAvatarFile(String fileName) {
        String mergedString = avatarsUri.concat(fileName).concat(".png");
        Path path = Paths.get(mergedString);
        try{
            Files.delete(path);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to get resource %s".formatted(path));
        }
    }
}
