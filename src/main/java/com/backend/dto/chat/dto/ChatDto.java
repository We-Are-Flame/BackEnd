package com.backend.dto.chat.dto;

import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.MessageType;
import com.backend.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDto {

    private MessageType messageType; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 유저 이름
    private Long senderId; // 유저 Id
    private String message; // 메시지
    private String time; // 채팅 발송 시간간

    public static ChatMessage of(ChatDto chatDto, ChatRoom chatRoom, User sender){
        return ChatMessage.builder()
                .messageType(chatDto.getMessageType())
                .chatRoom(chatRoom)
                .sender(sender)
                .message(chatDto.getMessage())
                .build();
    }
    public static ChatDto from(ChatMessage chatMessage){
        return ChatDto.builder()
                .messageType(chatMessage.getMessageType())
                .roomId(chatMessage.getChatRoom().getUuid())
                .sender(chatMessage.getSender().getNickname())
                .senderId(chatMessage.getSender().getId())
                .message(chatMessage.getMessage())
                .time(String.valueOf(chatMessage.getCreatedAt()))
                .build();
    }


}