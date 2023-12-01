package com.backend.service.chat;

import static com.backend.util.mapper.chat.RoomResponseMapper.buildChatRoom;
import static com.backend.util.mapper.chat.RoomResponseMapper.buildChatRoomUser;
import static com.backend.util.mapper.chat.RoomResponseMapper.buildRoomCreateMessage;
import static com.backend.util.mapper.chat.RoomResponseMapper.toChatRoomUser;
import static com.backend.util.mapper.chat.RoomResponseMapper.toChatUserResponses;
import static com.backend.util.mapper.chat.RoomResponseMapper.toRoomResponses;

import com.backend.dto.chat.request.create.RoomCreateRequest;
import com.backend.dto.chat.response.read.ChatUserResponse;
import com.backend.dto.chat.response.read.ChatUserResponseList;
import com.backend.dto.chat.response.read.RoomResponse;
import com.backend.dto.chat.response.read.RoomResponseList;
import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.ChatRoomUser;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.exception.AccessDeniedException;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.chat.ChatMessageRepository;
import com.backend.repository.chat.ChatRoomRepository;
import com.backend.repository.chat.ChatRoomUserRepository;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.service.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userService.fetchUser(userId);
        List<RoomResponse> roomResponses = toRoomResponses(user);

        return new RoomResponseList(roomResponses, roomResponses.size());
    }

    @Transactional
    public String createChatRoom(RoomCreateRequest request, Long userId) {
        Meeting meeting = fetchMeeting(request);
        User user = userService.fetchUser(userId);

        checkIsMeetingOwner(meeting, user);
        checkDuplicateChatRoom(meeting);

        ChatRoom chatRoom = buildChatRoom(request, meeting);
        chatRoomRepository.save(chatRoom);
        addUserInChatRoom(user.getId(), chatRoom.getUuid());

        ChatMessage roomCreateMessage = buildRoomCreateMessage(chatRoom, user);
        chatMessageRepository.save(roomCreateMessage);

        return chatRoom.getUuid();
    }

    @Transactional
    public Long deleteChatRoom(Long userId, String roomId) {
        User user = userService.fetchUser(userId);
        ChatRoom chatRoom = fetchRoom(roomId);
        ChatRoomUser chatRoomUser = toChatRoomUser(user, chatRoom);

        checkIsChatRoomOwner(chatRoomUser);

        Long chatRoomId = chatRoom.getId();
        chatRoomRepository.deleteById(chatRoom.getId());
        user.deleteRoomUser(chatRoomUser);

        return chatRoomId;
    }

    @Transactional
    public Long addUserInChatRoom(Long userId, String roomId) {
        User user = userService.fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);

        checkUserAlreadyInChatRoom(user, room);

        ChatRoomUser chatRoomUser = buildChatRoomUser(room, user);
        chatRoomUserRepository.save(chatRoomUser);

        user.addChatUser(chatRoomUser);
        room.addRoomUser(chatRoomUser);
        return user.getId();
    }

    @Transactional
    public Long exitUserFromChatRoom(Long userId, String roomId) {
        User user = userService.fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);

        ChatRoomUser chatRoomUser = findChatRoomUser(room, user);

        user.deleteRoomUser(chatRoomUser);
        room.deleteRoomUser(chatRoomUser);
        chatRoomUserRepository.deleteById(chatRoomUser.getId());

        checkAndDeleteEmptyRoom(room);
        return user.getId();
    }

    public ChatUserResponseList getRoomUserList(String roomId) {
        ChatRoom room = fetchRoom(roomId);
        List<ChatUserResponse> chatUserResponses = toChatUserResponses(room);
        return new ChatUserResponseList(chatUserResponses, chatUserResponses.size());
    }


    private ChatRoomUser findChatRoomUser(ChatRoom room, User user) {
        return chatRoomUserRepository.findByChatRoomAndUser(room, user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND_IN_CHAT_ROOM));
    }

    private ChatRoom fetchRoom(String roomId) {
        return chatRoomRepository.findByUuid(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROOM_NOT_FOUND));
    }

    private Meeting fetchMeeting(RoomCreateRequest request) {
        return meetingRepository.findById(request.getMeetingId())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private void checkDuplicateChatRoom(Meeting meeting) {
        if (chatRoomRepository.findByMeeting(meeting).isPresent()) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_EXIST_CHAT_ROOM);
        }
    }

    private void checkIsChatRoomOwner(ChatRoomUser chatRoomUser) {
        if (chatRoomUser.getIsOwner().equals(false)) {
            throw new BadRequestException(ErrorMessages.USER_DOES_NOT_OWN_CHAT_ROOM);
        }
    }

    private void checkUserAlreadyInChatRoom(User user, ChatRoom room) {
        if (chatRoomUserRepository.findByChatRoomAndUser(room, user).isPresent()) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_EXIST_CHAT_USER);
        }
    }

    private void checkIsMeetingOwner(Meeting meeting, User user) {
        if (!meeting.getHost().equals(user)) {
            throw new AccessDeniedException(ErrorMessages.ACCESS_DENIED);
        }
    }

    private void checkAndDeleteEmptyRoom(ChatRoom room) {
        if (room.getRoomUsers().isEmpty()) {
            chatRoomRepository.deleteById(room.getId());
        }
    }
}
