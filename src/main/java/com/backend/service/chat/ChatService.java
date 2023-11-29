package com.backend.service.chat;

import com.backend.dto.chat.request.create.ChatCreateRequest;
import com.backend.dto.chat.response.read.ChatResponse;
import com.backend.dto.chat.response.read.ChatResponseList;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.MessageType;
import com.backend.entity.user.User;
import com.backend.repository.chat.ChatMessageRepository;
import com.backend.repository.user.UserRepository;
import com.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final RoomService roomService;
    private final UserService userService;


    // 채팅방 삭제에 따른 채팅방의 사진 삭제를 위한 fileService 선언
//    private final FileService fileService;

    public void saveMessage(ChatCreateRequest chatRequest, Long userId) {
        User sender = userService.validateUser(userId);
        ChatRoom chatRoom = roomService.validateRoom(chatRequest.getRoomId());

        ChatMessage chatMessage = ChatMessage.of(chatRequest, chatRoom, sender);
        chatMessageRepository.save(chatMessage);
        chatRoom.addMessage(chatMessage);
        log.info("메세지 저장 완료");
    }

    public ChatCreateRequest createLeaveMessage(Long userId, String roomId) {
        User user = userService.validateUser(userId);
        log.info("User Disconnected : " + user.getNickname());

        return ChatCreateRequest.builder()
                .messageType(MessageType.LEAVE)
                .roomId(roomId)
                .sender(user.getNickname())
                .senderId(user.getId())
                .message(user.getNickname() + " 님이 퇴장 했습니다.")
                .time(String.valueOf(LocalDateTime.now()))
                .build();
    }

    public ChatResponseList findRoomMessages(String roomId) {
        ChatRoom chatRoom = roomService.validateRoom(roomId);
        List<ChatMessage> chatMessagesInChatRoom = chatMessageRepository.findAllByChatRoom(chatRoom);
        List<ChatResponse> chatResponses = chatMessagesInChatRoom
                .stream()
                .map(ChatResponse::from)
                .toList();
        return new ChatResponseList(chatResponses, chatResponses.size());
    }
}
