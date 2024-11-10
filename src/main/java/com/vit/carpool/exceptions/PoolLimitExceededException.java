package com.vit.carpool.exceptions;

public class PoolLimitExceededException extends RuntimeException {
    public PoolLimitExceededException(String message) {
        super(message);
    }
}
