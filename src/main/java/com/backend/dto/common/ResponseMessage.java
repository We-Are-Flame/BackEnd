package com.backend.dto.common;

public enum ResponseMessage {
    MEETING_CREATION("모임을 성공적으로 등록하셨습니다."),
    DELETE_MEETING("모임을 성공적으로 삭제하였습니다."),
    COMMENT_CREATION("댓글을 성공적으로 등록하였습니다."),
    APPLY_MEETING("모임에 성공적으로 신청하셨습니다."),
    CANCEL_MEETING("모임 신청을 성공적으로 취소하셨습니다."),
    APPLY_ACCEPT("모임 신청을 수락하셨습니다."),
    APPLY_REJECT("모임 신청을 거부하셨습니다."),
    NICKNAME_UPDATE_SUCCESS("닉네임을 성공적으로 변경하셨습니다."),
    PROFILE_IMAGE_UPDATE_SUCCESS("프로필 이미지를 성공적으로 변경하셨습니다."),
    NOTIFICATION_UPDATE_SUCCESS("유저 알림 설정을 성공적으로 변경하셨습니다."),
    TOKEN_VALIDATE_SUCCESS("토큰이 유효합니다."),
    CHAT_ROOM_CREATION_SUCCESS("채팅방이 성공적으로 생성되었습니다."),
    CHAT_ROOM_DELETE_SUCCESS("채팅방이 성공적으로 삭제되었습니다."),
    CHAT_ROOM_USER_ENTER_SUCCESS("유저가 채팅방에 성공적으로 입장했습니다."),
    CHAT_ROOM_USER_EXIT_SUCCESS("유저가 채팅방에서 성공적으로 퇴장되었습니다."),
    CHAT_ROOM_NOTIFICATION_UPDATE_SUCCESS("해당 유저의 채팅방 알림이 성공적으로 변경되었습니다.");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

