package com.example.app.services;

import com.example.app.entities.Game;
import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.enums.UserRole;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.repositories.GameRepository;
import com.example.app.repositories.TicketRepository;
import com.example.app.repositories.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final SecurityContext securityContext;

    @Inject
    public GameService(GameRepository gameRepository, UserRepository userRepository, TicketRepository ticketRepository, SecurityContext securityContext) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.securityContext = securityContext;
    }

    public Optional<Game> find(UUID id){
        return gameRepository.find(id);
    }
    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public List<Game> findAll(){
        return gameRepository.findAll();
    }
    public List<Ticket> getAllTickets(UUID id){
        if(securityContext.isCallerInRole(UserRole.ADMIN)){
            return gameRepository.findAllTickets(id);
        }
        Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
        if(user.isEmpty()){
            throw new HttpRequestException(401);
        }
        return gameRepository.findAllTicketsByUser(id, user.get());
    }
    public List<Ticket> getAllTickets(UUID id, Double stake, Boolean status){
        if(securityContext.isCallerInRole(UserRole.ADMIN)){
            return gameRepository.findAllTickets(id, stake, status);
        }
        Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
        if(user.isEmpty()){
            throw new HttpRequestException(401);
        }
        return gameRepository.findAllTicketsByUser(id, user.get(), stake, status);
    }
    @RolesAllowed(UserRole.ADMIN)
    public void createGame(Game game){
        gameRepository.create(game);
    }
    public void updateGame(Game game){
        gameRepository.update(game);
    }
    @RolesAllowed(UserRole.ADMIN)
    public void deleteGame(UUID id){
        gameRepository.delete(id);
    }
//    public void updateTicket(UUID gameId, Ticket ticket){
//        gameRepository.updateTicket(gameId, ticket);
//    }
}
