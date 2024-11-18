package com.example.app.services;

import com.example.app.entities.User;
import com.example.app.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@ApplicationScoped
@NoArgsConstructor(force = true)
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
    @Transactional
    public void createUser(User user){
        repository.create(user);
    }
    @Transactional
    public void updateUser(User user){
        repository.update(user);
    }
    @Transactional
    public void deleteUser(UUID id){
        repository.delete(id);
    }

//    public byte[] getUsersAvatar(UUID id){
//        return repository.getAvatar(id);
//    }
//    public void putUsersAvatar(UUID id, byte[] avatar){
//        repository.putAvatar(id, avatar);
//    }
//    public void deleteUsersAvatar(UUID id){
//        repository.deleteAvatar(id);
//    }

}
