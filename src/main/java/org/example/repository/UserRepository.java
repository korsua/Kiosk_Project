package org.example.repository;

import org.example.model.User;

import java.sql.SQLException;

public interface UserRepository {
    User save(String userId, String userPw);
    User findById(String userId) throws SQLException;


}
