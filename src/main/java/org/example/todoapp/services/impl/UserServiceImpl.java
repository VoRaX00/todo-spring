package org.example.todoapp.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.User;
import org.example.todoapp.models.UserDetailsImpl;
import org.example.todoapp.repositories.RoleRepository;
import org.example.todoapp.repositories.UserRepository;
import org.example.todoapp.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(String.format(
                "User %s not found",
                email)));
        return UserDetailsImpl.build(user);
    }

    @Override
    @Transactional
    public void createUser(UserRegisterDto userRegisterDto) {
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()) {
            throw new ConflictException(String.format(
                "User with email %s already exists",
                userRegisterDto.getEmail()
            ));
        }

        var user = userMapper.toModel(userRegisterDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(roleRepository.findByName("USER")
            .orElseThrow(() -> new InternalServerException("Role not found"))));
        userRepository.save(user);
    }

    @Override
    public Long findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(User::getId)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
