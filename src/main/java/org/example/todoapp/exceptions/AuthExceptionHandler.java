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
    public ResponseError handle(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handle(ConflictException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handle(InternalServerException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handle(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
