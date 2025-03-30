package org.example.todoapp.exceptions;

import org.springframework.http.*;
import org.springframework.web.server.*;

public class BadCredentialsException extends ResponseStatusException {
    public BadCredentialsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
