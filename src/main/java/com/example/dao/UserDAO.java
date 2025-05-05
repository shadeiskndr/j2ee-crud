package com.example.dao;

import com.example.model.User;

public interface UserDAO {
    User findByUsername(String username);
    User findByEmail(String email);
    int save(User user);
}
