package com.backend.dto.common;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final String message;

    public ErrorResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
}
