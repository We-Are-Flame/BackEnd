package com.backend.dto.chat.request.create;

import com.backend.entity.chat.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatCreateRequest {

    private MessageType messageType; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 유저 이름
    private Long senderId; // 유저 Id
    private String message; // 메시지
    private String time; // 채팅 발송 시간

    public void updateMessage(String message){
        this.message = message;
    }

}
