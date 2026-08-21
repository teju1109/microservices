package com.example.userservice.service;

import com.example.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public UserService() {
        users.put(1001L, new User(1001L, "John", "john@example.com"));
        users.put(1002L, new User(1002L, "Alice", "alice@example.com"));
        users.put(1003L, new User(1003L, "Bob", "bob@example.com"));
    }

    public User getUserById(Long id) {
        return users.get(id);
    }
}