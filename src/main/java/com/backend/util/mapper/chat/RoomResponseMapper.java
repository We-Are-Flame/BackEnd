package com.backend.util.mapper.chat;

import com.backend.dto.chat.request.create.RoomCreateRequest;
import com.backend.dto.chat.response.read.ChatUserResponse;
import com.backend.dto.chat.response.read.RoomResponse;
import com.backend.dto.chat.response.read.output.RoomStatusOutput;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.ChatRoomUser;
import com.backend.entity.chat.MessageType;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import java.util.List;
import java.util.UUID;

public class RoomResponseMapper {

    public static List<RoomResponse> toRoomResponses(User user) {
        return user.getChatRoomUsers().stream()
                .map(roomUser -> {
                    ChatRoom chatRoom = roomUser.getChatRoom();
                    List<ChatMessage> chatMessages = chatRoom.getMessages();
                    ChatMessage lastMessage = chatMessages.get(chatMessages.size() - 1);
                    RoomStatusOutput status = buildChatRoomStatus(chatRoom.getMeeting(), roomUser);
                    return RoomResponse.of(roomUser, chatRoom, lastMessage, status);
                })
                .toList();
    }

    public static ChatRoomUser toChatRoomUser(User user, ChatRoom chatRoom) {
        return chatRoom.getRoomUsers().stream()
                .filter(cu -> cu.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND_IN_CHAT_ROOM));
    }

    public static List<ChatUserResponse> toChatUserResponses(ChatRoom room) {
        return room.getRoomUsers()
                .stream()
                .map(ChatRoomUser::getUser)
                .map(ChatUserResponse::from)
                .toList();
    }

    public static ChatMessage buildRoomCreateMessage(ChatRoom room, User user) {
        return ChatMessage.builder()
                .messageType(MessageType.NOTICE)
                .message("모임이 개설 되었습니다!")
                .chatRoom(room)
                .sender(user)
                .build();
    }

    public static ChatRoom buildChatRoom(RoomCreateRequest request, Meeting meeting) {
        return ChatRoom.builder()
                .uuid(UUID.randomUUID().toString())
                .chatRoomName(request.getRoomName())
                .meeting(meeting)
                .build();
    }

    public static ChatRoomUser buildChatRoomUser(ChatRoom room, User user) {
        return ChatRoomUser.builder()
                .chatRoom(room)
                .user(user)
                .isOwner(Boolean.TRUE)
                .isRoomNotification(Boolean.TRUE)
                .build();
    }

    public static RoomStatusOutput buildChatRoomStatus(Meeting meeting, ChatRoomUser user) {
        return RoomStatusOutput.builder()
                .isOwner(user.getIsOwner())
                .isExpire(meeting.isExpired())
                .build();
    }

}
