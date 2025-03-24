package org.example.todoapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadCredentialsException extends ResponseStatusException {
    public BadCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
