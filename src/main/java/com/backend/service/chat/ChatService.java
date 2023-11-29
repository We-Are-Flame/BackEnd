package com.backend.service.chat;

import com.backend.dto.chat.request.create.ChatCreateRequest;
import com.backend.dto.chat.response.read.ChatResponse;
import com.backend.dto.chat.response.read.ChatResponseList;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.chat.ChatMessageRepository;
import com.backend.repository.chat.ChatRoomRepository;
import com.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.util.mapper.chat.RoomRequestMapper.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public void saveMessage(ChatCreateRequest chatRequest, Long userId) {
        User sender = userService.fetchUser(userId);
        ChatRoom chatRoom = fetchRoom(chatRequest.getRoomId());

        ChatMessage chatMessage = toChatMessage(chatRequest, chatRoom, sender);
        chatMessageRepository.save(chatMessage);
        chatRoom.addMessage(chatMessage);
    }

    public ChatCreateRequest createLeaveMessage(Long userId, String roomId) {
        User user = userService.fetchUser(userId);
        return buildLeaveRoomMessage(roomId, user);
    }

    public ChatResponseList findRoomMessages(String roomId) {
        ChatRoom chatRoom = fetchRoom(roomId);
        List<ChatMessage> chatMessagesInChatRoom = chatMessageRepository.findAllByChatRoom(chatRoom);
        List<ChatResponse> chatResponses = toChatResponses(chatMessagesInChatRoom);

        return new ChatResponseList(chatResponses, chatResponses.size());
    }

    private ChatRoom fetchRoom(String roomId) {
        return chatRoomRepository.findByUuid(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROOM_NOT_FOUND));
    }
}
