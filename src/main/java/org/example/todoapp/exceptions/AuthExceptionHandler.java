package org.example.todoapp.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.example.todoapp.errors.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handle(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
}
