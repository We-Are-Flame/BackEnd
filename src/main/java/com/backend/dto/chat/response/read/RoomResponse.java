package com.backend.dto.chat.response.read;

import com.backend.dto.chat.response.read.output.RoomStatusOutput;
import com.backend.dto.meeting.response.read.output.StatusOutput;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.ChatRoomUser;
import com.backend.entity.meeting.Meeting;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RoomResponse {
    @NotNull
    private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private String lastMessage ; // 마지막 메세지
    private LocalDateTime lastDateTime; // 마지막 메세지 시간
    private Boolean isNotification; // 채팅방 알림 설정
    private RoomStatusOutput status; // 채팅방 상태

    public static RoomResponse of(ChatRoomUser chatRoomUser, ChatRoom chatRoom, ChatMessage chatMessage, RoomStatusOutput status){
        return RoomResponse.builder()
                .roomId(chatRoom.getUuid())
                .roomName(chatRoom.getChatRoomName())
                .lastMessage(chatMessage.getMessage())
                .lastDateTime(chatMessage.getCreatedAt())
                .isNotification(chatRoomUser.getIsRoomNotification())
                .status(status)
                .build();
    }
}
