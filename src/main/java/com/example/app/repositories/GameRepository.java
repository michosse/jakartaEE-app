package com.example.app.repositories;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.exceptions.HttpRequestException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
public class GameRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em){
        this.em = em;
    }
    public Optional<Game> find(UUID id){
        return Optional.ofNullable(em.find(Game.class, id));
    }
    public List<Game> findAll(){
        return em.createQuery("select g from Game g", Game.class).getResultList();
    }
    public void create(Game game){
        em.persist(game);
    }
    public void delete(UUID id){
        if(this.find(id).isEmpty()){
            throw new HttpRequestException(404);
        }
        em.remove(em.find(Game.class, id));
    }
    public void update(Game game){
        em.merge(game);
    }
    public void addTicket(Ticket ticket){
       Optional<Game> game = Optional.ofNullable(em.find(Game.class,ticket.getGame().getId()));
       if(game.isPresent()){
           game.get().getTickets().add(ticket);
       }
    }
//    public void deleteTicket(UUID ticketId, UUID gameId){
//        Optional<Game> game = this.find(gameId);
//        if(game.isPresent()){
//            Optional<Ticket> ticket = game.get().getTickets().stream().filter(t->t.getId().equals(ticketId)).findFirst();
//            ticket.ifPresent(value -> game.get().getTickets().remove(value));
//        }
//        else {
//            throw new HttpRequestException(404);
//        }
//    }
//
//    public void updateTicket(UUID gameId, Ticket ticket){
//        Optional<Game> game = this.find(gameId);
//        if(game.isPresent()){
//            Optional<Ticket> ticketToChange = game.get().getTickets().stream().filter(t->t.getId().equals(ticket.getId())).findFirst();
//            if(ticketToChange.isPresent()){
//                ticketToChange.get().setStake(ticket.getStake());
//                ticketToChange.get().setWon(ticket.isWon());
//            }
//        }
//    }
    public List<Ticket> findAllTickets(UUID id) {
        Optional<Game> game = Optional.ofNullable(em.find(Game.class, id));
        if(game.isPresent()){
            return em.createQuery("select t from Ticket t where t.game.id = :id", Ticket.class).setParameter("id", id).getResultList();
        }
        throw new HttpRequestException(404);
    }
}
