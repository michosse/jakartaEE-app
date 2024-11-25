package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Dependent
public class UserRepository {
    private EntityManager em;
    @PersistenceContext
    public void setEm(EntityManager em){
        this.em = em;
    }
    public Optional<User> find(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }
    public Optional<User> findByLogin(String login){
        try {
            return Optional.of(em.createQuery("select u from User u where u.name = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }
    public void create(User user){
        em.persist(user);
    }
    public void delete(UUID id){
        if(this.find(id).isEmpty()){
            throw new HttpRequestException(404);
        }
        em.remove(em.find(User.class,id));
    }
    public void update(User user){
       em.merge(user);

    }
    public void addTicket(Ticket ticket){
        Optional<User> user = Optional.ofNullable(em.find(User.class,ticket.getUser().getId()));
        if(user.isPresent()){
            user.get().getTickets().add(ticket);
        }
    }
//
//    public void deleteTicket(UUID ticketId, UUID userId){
//        Optional<User> user = this.find(userId);
//        if(user.isPresent()){
//            Optional<Ticket> ticket = user.get().getTickets().stream().filter(t->t.getId().equals(ticketId)).findFirst();
//            ticket.ifPresent(value -> user.get().getTickets().remove(value));
//        }
//        else {
//            throw new HttpRequestException(404);
//        }
//    }
//
//    public void putAvatar(UUID id, byte[] avatar) {
//        Optional<User> userToChange = users.stream().filter(u -> u.getId().equals(id)).findFirst();
//        if(userToChange.isPresent()){
//            try{
//                userToChange.get().setAvatar(avatar);
//                saveBytesToFile(userToChange.get().getAvatar(), userToChange.get().getName());
//            } catch (IllegalStateException e){
//                throw new HttpRequestException("Cannot save file",400);
//            }
//        }else {
//            throw new HttpRequestException("User not found", 404);
//        }
//    }
//    public byte[] getAvatar(UUID id){
//        Optional<User> userToChange = users.stream().filter(u -> u.getId().equals(id)).findFirst();
//        if(userToChange.isPresent() && userToChange.get().getAvatar() != null){
//            return userToChange.get().getAvatar();
//        }else {
//            throw new HttpRequestException("Avatar not found",404);
//        }
//    }
//    public void deleteAvatar(UUID id){
//        Optional<User> userToChange = users.stream().filter(u->u.getId().equals(id)).findFirst();
//        if(userToChange.isPresent()){
//            userToChange.get().setAvatar(null);
//            try{
//            deleteAvatarFile(userToChange.get().getName());
//            } catch (IllegalStateException e){
//                throw new HttpRequestException("Avatar not found", 404);
//            }
//        }else {
//            throw new HttpRequestException("User not found",404);
//        }
//    }
//
//    private void saveBytesToFile(byte[] avatar, String fileName) {
//        String mergedString = avatarsUri.concat(fileName).concat(".png");
//        Path path = Paths.get(mergedString);
//        try{
//            Files.write(path,avatar, StandardOpenOption.CREATE);
//        } catch (IOException e) {
//            throw new IllegalStateException("Unable to get resource %s".formatted(path));
//        }
//    }
//    private void deleteAvatarFile(String fileName) {
//        String mergedString = avatarsUri.concat(fileName).concat(".png");
//        Path path = Paths.get(mergedString);
//        try{
//            Files.delete(path);
//        } catch (IOException e) {
//            throw new IllegalStateException("Unable to get resource %s".formatted(path));
//        }
//    }
}
