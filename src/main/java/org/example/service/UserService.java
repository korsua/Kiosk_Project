package org.example.service;


import org.example.model.User;
import org.example.repository.UserJdbcRepository;
import org.example.repository.UserRepository;

public class UserService {
    private static UserRepository repository = null;

    public UserService() {
        repository = new UserJdbcRepository();
    }

    public User join(String userId, String userPw) {
        return repository.save(userId,userPw);
    }
    public User loginById(String userId){
        return repository.findById(userId);
    }
}
