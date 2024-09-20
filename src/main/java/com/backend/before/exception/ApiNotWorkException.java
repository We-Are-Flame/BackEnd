package com.backend.before.exception;

public class ApiNotWorkException extends RuntimeException {
    public ApiNotWorkException(String message) {
        super(message);
    }
}
