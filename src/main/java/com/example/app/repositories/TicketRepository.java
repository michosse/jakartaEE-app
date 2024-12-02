package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Dependent
public class TicketRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em){
        this.em = em;
    }

    public Optional<Ticket> find(UUID id){
        return Optional.ofNullable(em.find(Ticket.class, id));
    }
    public List<Ticket> findAll() {
        return em.createQuery("select t from Ticket t", Ticket.class).getResultList();
    }
    public Optional<Ticket> findByUser(UUID id, User user){
        Optional<Ticket> ticket = this.find(id);
        if(ticket.isEmpty()){
            throw new HttpRequestException(404);
        }
        if(ticket.get().getUser().equals(user)){
            return ticket;
        }
        throw new HttpRequestException(403);
    }
    public List<Ticket> findAllByUser(User user){
        return em.createQuery("select t from Ticket  t where t.user = :user", Ticket.class)
                .setParameter("user", user)
                .getResultList();
    }
    public void create(Ticket ticket){
        em.persist(ticket);
        Optional<Game> game = Optional.ofNullable(em.find(Game.class,ticket.getGame().getId()));
        Optional<User> user = Optional.ofNullable(em.find(User.class,ticket.getUser().getId()));
        if(game.isPresent()){
            game.get().getTickets().add(ticket);
        }
        if(user.isPresent()){
            user.get().getTickets().add(ticket);
        }

    }
    public void delete(UUID id){
        if(this.find(id).isEmpty()){
            throw new HttpRequestException(404);
        }
        em.remove(em.find(Ticket.class,id));
    }
    public void update(Ticket ticket){
       em.merge(ticket);
    }

    @SneakyThrows
    private Ticket clone(Ticket ticket){
        try(ByteArrayInputStream is = new ByteArrayInputStream(writeObject(ticket).toByteArray());
            ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Ticket) ois.readObject();
        }
    }
    private ByteArrayOutputStream writeObject(Ticket object) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(object);
            return os;
        }
    }
}
