package org.example.todoapp.controllers;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.RequiredArgsConstructor;
import org.example.todoapp.dto.JwtResponse;
import org.example.todoapp.dto.UserLoginDto;
import org.example.todoapp.dto.UserRegisterDto;
import org.example.todoapp.services.UserService;
import org.example.todoapp.utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth-controller")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация пользователя в приложении")
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDto userRegisterDto) {
        userService.createUser(userRegisterDto);
        var link = "http://example.com";
        return new ResponseEntity<>(link, HttpStatus.CREATED);
    }

    @Operation(summary = "Аутентификация пользователя в приложении")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserLoginDto userLoginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword())
        );
        UserDetails userDetails = userService.loadUserByUsername(userLoginDto.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }
}
