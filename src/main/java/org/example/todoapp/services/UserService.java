package org.example.todoapp.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.todoapp.exceptions.InternalServerException;
import org.example.todoapp.models.User;
import org.example.todoapp.models.UserDetailsImpl;
import org.example.todoapp.repositories.RoleRepository;
import org.example.todoapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
        return UserDetailsImpl.build(user);
    }

    public void createUser(User user) {
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new InternalServerException("Role not found"))));
        userRepository.save(user);
    }
}
