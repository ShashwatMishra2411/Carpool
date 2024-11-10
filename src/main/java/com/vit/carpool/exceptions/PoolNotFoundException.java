package com.vit.carpool.exceptions;

public class PoolNotFoundException extends RuntimeException {
    public PoolNotFoundException(String message) {
        super(message);
    }
}