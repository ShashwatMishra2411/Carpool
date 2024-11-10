package com.vit.carpool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;






@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PoolLimitExceededException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handlePoolLimitExceededException(PoolLimitExceededException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidPoolDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPoolDataException(InvalidPoolDataException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(PoolNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePoolNotFoundException(PoolNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }
}
