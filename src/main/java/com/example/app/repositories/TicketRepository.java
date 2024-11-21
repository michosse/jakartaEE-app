package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.exceptions.HttpRequestException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
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
    public void create(Ticket ticket){
        em.persist(ticket);
        Optional<Game> game = Optional.ofNullable(em.find(Game.class,ticket.getGame().getId()));
        if(game.isPresent()){
            game.get().getTickets().add(ticket);
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
