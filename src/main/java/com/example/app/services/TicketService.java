package com.example.app.services;

import com.example.app.entities.Ticket;
import com.example.app.entities.User;
import com.example.app.enums.UserRole;
import com.example.app.exceptions.HttpRequestException;
import com.example.app.repositories.TicketRepository;
import com.example.app.repositories.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SecurityContext securityContext;

    @Inject
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, SecurityContext securityContext) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    public Optional<Ticket> find(UUID id){
        if(securityContext.isCallerInRole(UserRole.ADMIN)){
            return ticketRepository.find(id);
        }
        Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
        if(user.isEmpty()){
            throw new HttpRequestException(401);
        }
        return ticketRepository.findByUser(id,user.get());
    }
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    public List<Ticket> findAll(){
        if(securityContext.isCallerInRole(UserRole.ADMIN)){
            return ticketRepository.findAll();
        }
        Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
        if(user.isEmpty()){
            return new ArrayList<>();
        }
        return ticketRepository.findAllByUser(user.get());
    }
    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public void createTicket(Ticket ticket){
        Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
        if(user.isEmpty()){
            throw new HttpRequestException(401);
        }
        if(ticket.getUser()==null){
            ticket.setUser(user.get());
        }
        ticketRepository.create(ticket);
    }
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    public void updateTicket(Ticket ticket){
        if(securityContext.isCallerInRole(UserRole.ADMIN)){
            ticketRepository.update(ticket);
        }
        else {
            Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
            if(user.isEmpty()){
                throw new HttpRequestException(401);
            }
            if(!ticket.getUser().getId().equals(user.get().getId())){
                throw new HttpRequestException(403);
            }
            ticketRepository.update(ticket);
        }

    }
    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    public void deleteTicket(UUID id){
        if(securityContext.isCallerInRole(UserRole.ADMIN)){
            ticketRepository.delete(id);
        }
        else {
            Optional<Ticket> ticket = ticketRepository.find(id);
            Optional<User> user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName());
            if(user.isEmpty()){
                throw new HttpRequestException(401);
            }
            if(ticket.isEmpty()){
                throw new HttpRequestException(404);
            }
            if(!ticket.get().getUser().getId().equals(user.get().getId())){
                throw new HttpRequestException(403);
            }
            ticketRepository.delete(id);
        }
    }


}
