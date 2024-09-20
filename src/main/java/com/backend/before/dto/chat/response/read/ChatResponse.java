package com.backend.before.dto.chat.response.read;

import com.backend.before.entity.chat.ChatMessage;
import com.backend.before.entity.chat.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatResponse {

    @JsonProperty("message_type")
    private MessageType messageType;
    @JsonProperty("room_id")
    private String roomId;
    private String sender;
    @JsonProperty("sender_id")
    private Long senderId;
    private String message;
    private String time;
    @JsonProperty("profile_image")
    private String profileImage;
    @JsonProperty("is_school_verified")
    private Boolean isSchoolVerified;

    public static ChatResponse from(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .messageType(chatMessage.getMessageType())
                .roomId(chatMessage.getChatRoom().getUuid())
                .sender(chatMessage.getSender().getNickname())
                .senderId(chatMessage.getSender().getId())
                .message(chatMessage.getMessage())
                .profileImage(chatMessage.getSender().getProfileImage())
                .time(String.valueOf(chatMessage.getCreatedAt()))
                .isSchoolVerified(chatMessage.getSender().getIsSchoolVerified())
                .build();
    }
}
