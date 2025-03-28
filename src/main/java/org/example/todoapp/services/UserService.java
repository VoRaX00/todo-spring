package org.example.todoapp.services;

import org.example.todoapp.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(UserRegisterDto userRegisterDto);
    Long findUserIdByEmail(String email);
}
