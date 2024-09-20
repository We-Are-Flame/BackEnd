package com.backend.before.util.mapper.chat;

import com.backend.before.dto.chat.request.create.ChatCreateRequest;
import com.backend.before.dto.chat.response.read.ChatResponse;
import com.backend.before.entity.chat.ChatMessage;
import com.backend.before.entity.chat.ChatRoom;
import com.backend.before.entity.user.User;
import java.util.List;

public class RoomRequestMapper {

    public static ChatMessage toChatMessage(ChatCreateRequest chatDto, ChatRoom chatRoom, User sender) {
        return ChatMessage.builder()
                .messageType(chatDto.getMessageType())
                .chatRoom(chatRoom)
                .sender(sender)
                .message(chatDto.getMessage())
                .build();
    }

    public static List<ChatResponse> toChatResponses(List<ChatMessage> chatMessagesInChatRoom) {
        return chatMessagesInChatRoom
                .stream()
                .map(ChatResponse::from)
                .toList();
    }

}
