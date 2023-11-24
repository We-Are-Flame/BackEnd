package com.backend.dto.bases;

public enum ResponseMessage {
    MEETING_CREATION_SUCCESS("모임 등록 성공"),
    COMMENT_CREATION_SUCCESS("댓글 등록 성공");


    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

