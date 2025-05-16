package com.example.dao;

import com.example.model.User;

public interface UserDAO {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(int id);
    int save(User user);
    boolean updatePassword(int userId, String hashedPassword);
}
