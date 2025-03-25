package org.example.todoapp.controllers;

import lombok.RequiredArgsConstructor;
import org.example.todoapp.dto.JwtResponse;
import org.example.todoapp.dto.UserLoginDto;
import org.example.todoapp.dto.UserRegisterDto;
import org.example.todoapp.mappers.UserMapper;
import org.example.todoapp.services.UserService;
import org.example.todoapp.utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDto userRegisterDto) {
        var user = UserMapper.INSTANCE.toModel(userRegisterDto);
        userService.createUser(user);
        var link = "";
        return new ResponseEntity<>(link, HttpStatus.CREATED);
    }

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
