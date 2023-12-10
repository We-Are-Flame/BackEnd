package com.backend.exception;

public class ApiNotWorkException extends RuntimeException {
    public ApiNotWorkException(String message) {
        super(message);
    }
}
