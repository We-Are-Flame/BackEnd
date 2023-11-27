package com.backend.dto.bases;

public enum ResponseMessage {
    MEETING_CREATION_SUCCESS("모임 등록 성공"),
    COMMENT_CREATION_SUCCESS("댓글 등록 성공"),
    REGISTRATION_CREATION_SUCCESS("모임에 성공적으로 신청하셨습니다."),
    NICKNAME_UPDATE_SUCCESS("닉네임을 성공적으로 변경하셨습니다."),
    PROFILE_IMAGE_UPDATE_SUCCESS("프로필 이미지를 성공적으로 변경하셨습니다.");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }

