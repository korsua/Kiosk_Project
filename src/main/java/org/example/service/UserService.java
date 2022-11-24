package org.example.service;


import org.example.model.User;
import org.example.repository.UserJdbcRepository;
import org.example.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private static UserRepository repository = null;
    private static UserService instance;

    private UserService(UserRepository userRepository){
        this.repository = userRepository;
    }
    public static synchronized UserService getInstance(){
        if(instance == null) instance = new UserService(new UserJdbcRepository());
        return instance;
    }

    public User join(String userId, String userPw) {
        return repository.save(userId,userPw);
    }
    public User loginById(String userId) throws SQLException {
        return repository.findById(userId);
    }
}
