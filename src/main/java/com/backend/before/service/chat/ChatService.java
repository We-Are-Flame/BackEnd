package com.backend.before.service.chat;

import static com.backend.before.util.mapper.chat.RoomRequestMapper.toChatMessage;
import static com.backend.before.util.mapper.chat.RoomRequestMapper.toChatResponses;

import com.backend.before.dto.chat.request.create.ChatCreateRequest;
import com.backend.before.dto.chat.response.read.ChatResponse;
import com.backend.before.dto.chat.response.read.ChatResponseList;
import com.backend.before.entity.chat.ChatMessage;
import com.backend.before.entity.chat.ChatRoom;
import com.backend.before.entity.user.User;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.before.repository.chat.ChatMessageRepository;
import com.backend.before.repository.chat.ChatRoomRepository;
import com.backend.before.service.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatResponse saveMessage(ChatCreateRequest chatRequest, Long userId) {
        User sender = userService.fetchUser(userId);
        ChatRoom chatRoom = fetchRoom(chatRequest.getRoomId());

        ChatMessage chatMessage = toChatMessage(chatRequest, chatRoom, sender);
        chatMessageRepository.save(chatMessage);
        chatRoom.addMessage(chatMessage);
        return ChatResponse.from(chatMessage);
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
