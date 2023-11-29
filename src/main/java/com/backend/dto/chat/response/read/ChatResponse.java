package com.backend.dto.chat.response.read;

import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.MessageType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatResponse {

    private MessageType messageType; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 유저 이름
    private Long senderId; // 유저 Id
    private String message; // 메시지
    private String time; // 채팅 발송 시간

    public static ChatResponse from(ChatMessage chatMessage){
        return ChatResponse.builder()
                .messageType(chatMessage.getMessageType())
                .roomId(chatMessage.getChatRoom().getUuid())
                .sender(chatMessage.getSender().getNickname())
                .senderId(chatMessage.getSender().getId())
                .message(chatMessage.getMessage())
                .time(String.valueOf(chatMessage.getCreatedAt()))
                .build();
    }
}
