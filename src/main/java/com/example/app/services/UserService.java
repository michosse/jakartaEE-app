package com.example.app.services;

import com.example.app.entities.User;
import com.example.app.enums.UserRole;
import com.example.app.repositories.UserRepository;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class UserService {
    private final UserRepository repository;
    private final Pbkdf2PasswordHash hash;
    @Inject
    public UserService(UserRepository repository, Pbkdf2PasswordHash hash) {
        this.repository = repository;
        this.hash = hash;
    }
    public Optional<User> find(UUID id){
        return repository.find(id);
    }
    public List<User> findAllUsers(){
        return repository.findAll();
    }
    public void createUser(User user){
        user.setPassword(hash.generate(user.getPassword().toCharArray()));
        if(user.getRoles() == null) {
            user.setRoles(List.of(UserRole.USER));
        }
        repository.create(user);
    }
    public void updateUser(User user){
        repository.update(user);
    }
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
