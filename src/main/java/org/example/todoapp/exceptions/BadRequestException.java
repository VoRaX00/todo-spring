package org.example.todoapp.exceptions;

import org.springframework.http.*;
import org.springframework.web.server.*;

public class BadRequestException extends ResponseStatusException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
