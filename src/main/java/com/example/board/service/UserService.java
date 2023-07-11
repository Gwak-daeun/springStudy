package com.example.board.service;

import org.apache.catalina.User;

public interface UserService {
    public User addUser(String name, String email, String password);
}
