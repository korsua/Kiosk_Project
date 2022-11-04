package org.example.service;

import org.example.repository.UserJdbcRepository;
import org.example.repository.UserRepository;

public class UserService {
    private static UserRepository repository = null;

    public UserService() {
        repository = new UserJdbcRepository();
    }
}
