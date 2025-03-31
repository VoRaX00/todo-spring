package org.example.todoapp.controllers;

import com.fasterxml.jackson.databind.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.BadCredentialsException;
import org.example.todoapp.services.*;
import org.example.todoapp.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.test.web.servlet.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    @MockBean
    private AuthenticationManager authenticationManager;


    @Test
    void singUp_ShouldReturnCreateStatus() throws Exception {
        var userRegisterDto = new UserRegisterDto(
            "test@example.com",
            "Test User",
            "password"
        );

        doNothing().when(userService).createUser(userRegisterDto);
        mockMvc.perform(post("/auth/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userRegisterDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value("http://example.com"));

        verify(userService, times(1)).createUser(userRegisterDto);
    }

    @Test
    void signIn_ShouldReturnJwtToken() throws Exception {
        var loginDto = new UserLoginDto(
            "test@example.com",
            "1324"
        );
        var userDetails = mock(UserDetails.class);
        var expectedToken = "test.jwt.token";

        when(userService.loadUserByUsername(loginDto.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn(expectedToken);

        mockMvc.perform(post("/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));

        verify(userService, times(1)).loadUserByUsername(loginDto.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenUtils).generateToken(userDetails);
    }

    @Test
    void signIn_ShouldReturnNotFound() throws Exception {
        var loginDto = new UserLoginDto(
            "test@example.com",
            "1324"
        );

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Bad Credentials"));

        mockMvc.perform(post("/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(loginDto)))
            .andExpect(status().isBadRequest());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

}
