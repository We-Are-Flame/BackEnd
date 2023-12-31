package com.backend.util.mapper.chat;

import com.backend.dto.chat.request.create.ChatCreateRequest;
import com.backend.dto.chat.response.read.ChatResponse;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.MessageType;
import com.backend.entity.user.User;
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
