package org.example.todoapp.services;

import org.example.todoapp.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(User user);
}
