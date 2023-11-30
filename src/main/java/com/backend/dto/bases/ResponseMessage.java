package com.backend.dto.bases;

public enum ResponseMessage {
    MEETING_CREATION_SUCCESS("모임 등록 성공"),
    COMMENT_CREATION_SUCCESS("댓글 등록 성공"),
    REGISTRATION_CREATION_SUCCESS("모임에 성공적으로 신청하셨습니다."),

    REGISTRATION_CANCEL_SUCCESS("모임 신청을 성공적으로 취소하셨습니다."),
    NICKNAME_UPDATE_SUCCESS("닉네임을 성공적으로 변경하셨습니다."),
    PROFILE_IMAGE_UPDATE_SUCCESS("프로필 이미지를 성공적으로 변경하셨습니다."),
    NOTIFICATION_UPDATE_SUCCESS("유저 알림 설정을 성공적으로 변경하셨습니다."),
    TOKEN_VALIDATE_SUCCESS("토큰이 유효합니다."),
    CHAT_ROOM_CREATION_SUCCESS("채팅방이 성공적으로 생성되었습니다."),
    CHAT_ROOM_DELETE_SUCCESS("채팅방이 성공적으로 삭제되었습니다."),
    CHAT_ROOM_USER_ENTER_SUCCESS("유저가 채팅방에 성공적으로 입장했습니다."),
    CHAT_ROOM_USER_EXIT_SUCCESS("유저가 채팅방에서 성공적으로 퇴장되었습니다.");


    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }

