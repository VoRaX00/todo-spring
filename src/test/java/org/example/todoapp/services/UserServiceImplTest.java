package org.example.todoapp.services;

import java.util.*;

import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.models.User;
import org.example.todoapp.repositories.*;
import org.example.todoapp.services.impl.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Test
    void loadUserByUsername_Success() {
        var user = User.builder()
            .id(1L).email("test@example.com")
            .username("test").password("test")
            .roles(List.of(new Role(1L, "ROLE_USER"))).build();
        var userDetails = UserDetailsImpl.build(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var result = userService.loadUserByUsername(user.getEmail());
        Assertions.assertEquals(userDetails, result);
    }

    @Test
    void loadUserByUsername_NotFound() {
        var user = User.builder()
            .id(1L).email("test@example.com")
            .username("test").password("test")
            .roles(List.of(new Role(1L, "ROLE_USER"))).build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(
            UsernameNotFoundException.class,
            () -> userService.loadUserByUsername(user.getEmail()));
    }

    @Test
    void createUser_Success() {
        var userRegisterDto = UserRegisterDto.builder()
            .email("test@example.com")
            .username("test").password("test").build();

        var user = User.builder()
            .email("test@example.com")
            .username("test").password("password").build();
        var role = new Role(1L, "USER");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toModel(userRegisterDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("password");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);

        userService.createUser(userRegisterDto);
        verify(userRepository, times(1)).save(user);
        Assertions.assertEquals(user.getRoles().getFirst(), role);
    }

    @Test
    void createUser_Conflict() {
        var userRegisterDto = UserRegisterDto.builder()
            .email("test@example.com")
            .username("test").password("test").build();
        var user = User.builder()
            .email("test@example.com")
            .username("test").password("password").build();

        when(userRepository.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertThrows(ConflictException.class, () -> userService.createUser(userRegisterDto));
    }

    @Test
    void createUser_InternalServerError() {
        var userRegisterDto = UserRegisterDto.builder()
            .email("test@example.com")
            .username("test").password("test").build();
        var user = User.builder()
            .email("test@example.com")
            .username("test").password("password").build();

        when(userRepository.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toModel(userRegisterDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("password");
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
        Assertions.assertThrows(InternalServerException.class, () -> userService.createUser(userRegisterDto));
    }

    @Test
    void findUserIdByEmail_Success() {
        var user = User.builder()
            .id(1L).email("test@example.com")
            .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        var result = userService.findUserIdByEmail(user.getEmail());
        Assertions.assertEquals(user.getId(), result);
    }

    @Test
    void findUserIdByEmail_NotFound() {
        var user = User.builder()
            .id(1L).email("test@example.com")
            .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.findUserIdByEmail(user.getEmail()));
    }
}
