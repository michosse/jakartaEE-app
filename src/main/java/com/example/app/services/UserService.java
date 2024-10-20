package com.example.app.services;

import com.example.app.entities.User;
import com.example.app.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@NoArgsConstructor(force = true)
@ApplicationScoped
public class UserService {
    private final UserRepository repository;
    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public Optional<User> find(UUID id){
        return repository.find(id);
    }
    public List<User> findAllUsers(){
        return repository.findAll();
    }
    public void createUser(User user){
        repository.create(user);
    }
    public void updateUser(User user){
        repository.update(user);
    }
    public void deleteUser(User user){
        repository.delete(user);
    }
    public byte[] getUsersAvatar(UUID id){
        return repository.getAvatar(id);
    }
    public void putUsersAvatar(UUID id, byte[] avatar){
        repository.putAvatar(id, avatar);
    }
    public void deleteUsersAvatar(UUID id){
        repository.deleteAvatar(id);
    }

}
