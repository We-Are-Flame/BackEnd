package com.backend.before.exception;

public class ErrorMessages {
    public static final String CATEGORY_NOT_FOUND = "카테고리를 찾을 수 없습니다.";
    public static final String MEETING_NOT_FOUND = "모임을 찾을 수 없습니다.";
    public static final String ALREADY_REGISTERED = "이미 모임에 신청하셨습니다.";
    public static final String NO_MATCH_EMAIL_AND_USER = "해당 이메일과 맞는 사용자가 존재하지 않습니다!";
    public static final String INVALID_PAYLOAD = "페이로드가 올바르지 않습니다.";
    public static final String NO_MATCH_TOKEN_AND_USER = "해당 토큰과 맞는 사용자가 존재하지 않습니다!";
    public static final String NOT_EXIST_USER = "로그인 후 진행해주셔야 합니다!";
    public static final String ACCESS_DENIED = "권한이 없습니다!";
    public static final String DUPLICATE_REQUEST = "중복 요청을 보내실 수 없습니다!";
    public static final String ROOM_NOT_FOUND = "해당 채팅방을 찾을 수 없습니다!";
    public static final String USER_NOT_FOUND_IN_CHAT_ROOM = "채팅방에서 해당 유저를 찾을 수 없습니다.";
    public static final String USER_DOES_NOT_OWN_CHAT_ROOM = "해당 채팅방의 방장이 아닙니다.";
    public static final String ALREADY_EXIST_CHAT_ROOM = "이미 해당 모임의 채팅방이 만들어져 있습니다.";
    public static final String ALREADY_EXIST_CHAT_USER = "이미 채팅방에 입장해 있습니다.";
    public static final String CANNOT_CANCEL_OWNER_REGISTRATION = "Host이시면 신청 취소를 하실 수 없습니다.";
    public static final String REGISTRATION_NOT_FOUND = "모임에 대한 신청 정보를 찾을 수 없습니다.";
    public static final String CAN_CHANGE_IN_PENDING = "PENDING상태에서만 변경될 수 있습니다!";
    public static final String NOT_EXIST_USERS = "id에 대한 사용자가 존재하지 않습니다.";
    public static final String UNABLE_TO_SEND_EMAIL = "학교인증 메일을 전송할 수 없습니다.";
    public static final String ALREADY_VERIFIED_EMAIL = "해당 유저는 이미 학교 인증 되었습니다.";
    public static final String ALREADY_CERTIFICATED_SCHOOL_EMAIL = "해당 이메일은 이미 학교 인증 되었습니다.";
    public static final String NO_SUCH_ALGORITHM = "랜덤 알고리즘을 찾을 수 없습니다.";
    public static final String NO_MATCH_AUTH_CODE = "인증 번호가 일치하지 않습니다.";
    public static final String KEYWORD_NULL = "해시태그, 또는 검색키워드가 없습니다!";
    public static final String INVALID_RATING = "유효하지 않은 별점입니다";
}

