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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Dependent
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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = cb.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);
        query.select(root);
        return em.createQuery(query).getResultList();
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
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.select(root).where(cb.equal(root.get("game").get("id"), id));
            return em.createQuery(query).getResultList();
        }
        throw new HttpRequestException(404);
    }

    public List<Ticket> findAllTicketsByUser(UUID id, User user) {
        Optional<Game> game = Optional.ofNullable(em.find(Game.class, id));
        if(game.isPresent()){
            return game.get().getTickets().stream().filter(t -> t.getUser().getId().equals(user.getId())).collect(Collectors.toList());
        }
        throw new HttpRequestException(404);
    }
}
