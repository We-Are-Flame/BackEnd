package com.backend.dto.chat.response.read;

import com.backend.dto.chat.response.read.output.RoomStatusOutput;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.ChatRoomUser;
import com.backend.entity.chat.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {

    @JsonProperty("room_id")
    private String roomId;

    @JsonProperty("room_name")
    private String roomName;

    @JsonProperty("last_message")
    private String lastMessage;

    @JsonProperty("last_message_type")
    private MessageType lastMessageType;

    @JsonProperty("last_date_time")
    private LocalDateTime lastDateTime;

    @JsonProperty("is_notification")
    private Boolean isNotification;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    private RoomStatusOutput status;

    public static RoomResponse of(ChatRoomUser chatRoomUser, ChatRoom chatRoom, ChatMessage chatMessage,
                                  RoomStatusOutput status) {
        return RoomResponse.builder()
                .roomId(chatRoom.getUuid())
                .roomName(chatRoom.getChatRoomName())
                .lastMessage(chatMessage.getMessage())
                .lastDateTime(chatMessage.getCreatedAt())
                .isNotification(chatRoomUser.getIsRoomNotification())
                .thumbnailUrl(chatRoom.getMeeting().getThumbnailUrl())
                .lastMessageType(chatMessage.getMessageType())
                .status(status)
                .build();
    }
}
