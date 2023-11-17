package com.backend.dto.bases;

public enum ResponseStatus {
    SUCCESS("성공"),
    FAIL("실패");

    private final String message;

    ResponseStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

