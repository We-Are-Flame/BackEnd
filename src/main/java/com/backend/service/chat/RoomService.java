package com.backend.service.chat;

import com.backend.dto.chat.request.create.RoomCreateRequest;
import com.backend.dto.chat.response.read.ChatUserResponse;
import com.backend.dto.chat.response.read.ChatUserResponseList;
import com.backend.dto.chat.response.read.RoomResponse;
import com.backend.dto.chat.response.read.RoomResponseList;
import com.backend.dto.chat.response.read.output.RoomStatusOutput;
import com.backend.dto.meeting.response.read.output.StatusOutput;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.ChatRoomUser;
import com.backend.entity.chat.MessageType;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.chat.ChatMessageRepository;
import com.backend.repository.chat.ChatRoomRepository;
import com.backend.repository.chat.ChatRoomUserRepository;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MeetingRepository meetingRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    @Transactional
    public RoomResponseList getMyChatRooms(Long userId) {
        User user = userService.validateUser(userId);
        List<RoomResponse> roomResponses = user.getChatRoomUsers().stream()
                .map(roomUser -> {
                    ChatRoom chatRoom = roomUser.getChatRoom();
                    List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom(chatRoom);
                    ChatMessage lastMessage = chatMessages.get(chatMessages.size() - 1);
                    RoomStatusOutput status = buildChatRoomStatus(chatRoom.getMeeting(), roomUser);
                    return RoomResponse.of(roomUser, chatRoom, lastMessage, status);
                })
                .toList();

        return new RoomResponseList(roomResponses, roomResponses.size());
    }

    @Transactional
    public Long createChatRoom(RoomCreateRequest request, Long userId) {
        Meeting meeting = meetingRepository.findById(request.getMeetingId())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));

        User user = userService.validateUser(userId);

        ChatRoom room = ChatRoom.builder()
                .uuid(UUID.randomUUID().toString())
                .chatRoomName(request.getRoomName())
                .meeting(meeting)
                .build();

        chatRoomRepository.save(room);
        addUser(room.getUuid(), user.getId());

        ChatMessage roomCreateMessage = ChatMessage.builder()
                .messageType(MessageType.NOTICE)
                .message("모임이 개설 되었습니다!")
                .chatRoom(room)
                .sender(user)
                .build();

        chatMessageRepository.save(roomCreateMessage);
        return room.getId();
    }

    @Transactional
    public Long deleteChatRoom(User user, String roomId) {
        // 대상 채팅방 검증
        ChatRoom chatRoom = validateRoom(roomId);

        ChatRoomUser chatRoomUser = chatRoom.getRoomUsers().stream()
                .filter(cu -> cu.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND_IN_CHAT_ROOM));

        if (!chatRoomUser.getIsOwner()) {
            throw new BadRequestException(ErrorMessages.USER_DOES_NOT_OWN_CHAT_ROOM);
        }

        Long deletedUserId = chatRoomUser.getId();
        chatRoomRepository.deleteById(chatRoom.getId());
        user.deleteRoomUser(chatRoomUser);

        return deletedUserId;
    }
    @Transactional
    public void addUser(String roomId, Long userId){
        User user = userService.validateUser(userId);
        ChatRoom room = validateRoom(roomId);

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(room)
                .user(user)
                .isOwner(Boolean.TRUE)
                .isRoomNotification(Boolean.TRUE)
                .build();
        chatRoomUserRepository.save(chatRoomUser);
        // TODO : 아래 중복
        user.addChatUser(chatRoomUser);
        room.addRoomUser(chatRoomUser);
    }

    @Transactional
    public void delUser(String roomId, Long userId){
        User user = userService.validateUser(userId);
        ChatRoom room = validateRoom(roomId);

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomAndUser(room, user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND_IN_CHAT_ROOM));

        user.deleteRoomUser(chatRoomUser);
        room.deleteRoomUser(chatRoomUser);

        chatRoomUserRepository.deleteById(chatRoomUser.getId());
    }

    public void delChatRoom(String roomId){
        chatRoomRepository.deleteByUuid(roomId);
        // 채팅방 안에 있는 파일 삭제
        //  fileService.deleteFileDir(roomId);
        log.info("삭제 완료 roomId : {}", roomId);
    }

    public ChatUserResponseList getRoomUserList(String roomId) {
        ChatRoom room = validateRoom(roomId);
        List<ChatUserResponse> chatUserResponses = room.getRoomUsers()
                .stream()
                .map(ChatRoomUser::getUser)
                .map(ChatUserResponse::from)
                .toList();

        return new ChatUserResponseList(chatUserResponses, chatUserResponses.size());
    }

    // TODO : 이것도 수정 하고 싶음
    private RoomStatusOutput buildChatRoomStatus(Meeting meeting, ChatRoomUser user) {
        return RoomStatusOutput.builder()
                .isOwner(user.getIsOwner())
                .isExpire(meeting.isExpired())
                .build();
    }
    public ChatRoom validateRoom(String roomId) {
        return chatRoomRepository.findByUuid(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROOM_NOT_FOUND));
    }



}
