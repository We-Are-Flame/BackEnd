package com.backend.before.service.chat;

import static com.backend.before.util.mapper.chat.RoomResponseMapper.buildChatRoom;
import static com.backend.before.util.mapper.chat.RoomResponseMapper.buildChatRoomUser;
import static com.backend.before.util.mapper.chat.RoomResponseMapper.buildNotificationResponse;
import static com.backend.before.util.mapper.chat.RoomResponseMapper.buildRoomCreateMessage;
import static com.backend.before.util.mapper.chat.RoomResponseMapper.toChatRoomUser;
import static com.backend.before.util.mapper.chat.RoomResponseMapper.toChatUserResponses;
import static com.backend.before.util.mapper.chat.RoomResponseMapper.toRoomResponses;

import com.backend.before.dto.chat.request.create.RoomCreateRequest;
import com.backend.before.dto.chat.request.update.RoomUpdateRequest;
import com.backend.before.dto.chat.response.read.ChatUserResponse;
import com.backend.before.dto.chat.response.read.ChatUserResponseList;
import com.backend.before.dto.chat.response.read.RoomDetailResponse;
import com.backend.before.dto.chat.response.read.RoomDetailResponse.Notification;
import com.backend.before.dto.chat.response.read.RoomDetailResponse.Title;
import com.backend.before.dto.chat.response.read.RoomResponse;
import com.backend.before.dto.chat.response.read.RoomResponseList;
import com.backend.before.entity.chat.ChatMessage;
import com.backend.before.entity.chat.ChatRoom;
import com.backend.before.entity.chat.ChatRoomUser;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.before.entity.user.User;
import com.backend.before.exception.AccessDeniedException;
import com.backend.before.exception.AlreadyExistsException;
import com.backend.before.exception.BadRequestException;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.before.repository.chat.ChatMessageRepository;
import com.backend.before.repository.chat.ChatRoomRepository;
import com.backend.before.repository.chat.ChatRoomUserRepository;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import com.backend.before.repository.user.UserRepository;
import com.backend.before.util.mapper.chat.RoomResponseMapper;
import java.util.ArrayList;
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
    private final UserRepository userRepository;

    @Transactional
    public RoomResponseList getMyChatRooms(Long userId) {
        User user = fetchUser(userId);
        List<RoomResponse> roomResponses = toRoomResponses(user);

        return new RoomResponseList(roomResponses, roomResponses.size());
    }

    @Transactional
    public String createChatRoom(RoomCreateRequest request, Long userId) {
        Meeting meeting = fetchMeeting(request.getMeetingId());
        User user = fetchUser(userId);

        checkIsMeetingOwner(meeting, user);
        checkDuplicateChatRoom(meeting);

        ChatRoom chatRoom = buildChatRoom(request, meeting);
        chatRoomRepository.save(chatRoom);
        addUserInChatRoom(user.getId(), meeting.getId(), chatRoom.getUuid());

        ChatMessage roomCreateMessage = buildRoomCreateMessage(chatRoom, user);
        chatMessageRepository.save(roomCreateMessage);

        return chatRoom.getUuid();
    }

    @Transactional
    public Long deleteChatRoom(Long userId, String roomId) {
        User user = fetchUser(userId);
        ChatRoom chatRoom = fetchRoom(roomId);
        ChatRoomUser chatRoomUser = toChatRoomUser(user, chatRoom);

        checkIsChatRoomOwner(chatRoomUser);

        Long chatRoomId = chatRoom.getId();
        chatRoomRepository.deleteById(chatRoomId);
        user.deleteRoomUser(chatRoomUser);

        return chatRoomId;
    }

    @Transactional
    public Long addUserInChatRoom(Long userId, Long meetingId, String roomId) {
        User user = fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);
        Meeting meeting = fetchMeeting(meetingId);

        checkUserAlreadyInChatRoom(user, room);

        Boolean isOwner = meeting.getHost().equals(user);
        ChatRoomUser chatRoomUser = buildChatRoomUser(room, user, isOwner);

        chatRoomUserRepository.save(chatRoomUser);

        user.addChatUser(chatRoomUser);
        room.addRoomUser(chatRoomUser);
        return user.getId();
    }

    @Transactional
    public Long exitUserFromChatRoom(Long userId, String roomId) {
        User user = fetchUser(userId);
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

    @Transactional
    public Long updateRoomNotification(RoomUpdateRequest.Notification request, Long userId, String roomId) {
        User user = fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);
        ChatRoomUser chatRoomUser = findChatRoomUser(room, user);
        chatRoomUser.updateRoomNotification(request.getIsNotification());
        return user.getId();
    }

    @Transactional
    public Long updateRoomTitle(RoomUpdateRequest.Title request, Long userId, String roomId) {
        User user = fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);
        ChatRoomUser chatRoomUser = findChatRoomUser(room, user);
        checkIsChatRoomOwner(chatRoomUser);
        room.updateChatRoomName(request.getTitle());
        return room.getId();
    }

    @Transactional
    public void addUsersInChatRoom(List<User> users, Long meetingId, String roomId) {
        ChatRoom room = fetchRoom(roomId);
        Meeting meeting = fetchMeeting(meetingId);
        List<ChatRoomUser> chatRoomUsers = new ArrayList<>();

        for (User user : users) {
            checkUserAlreadyInChatRoom(user, room);
            Boolean isOwner = meeting.getHost().equals(user);

            ChatRoomUser chatRoomUser = buildChatRoomUser(room, user, isOwner);

            chatRoomUsers.add(chatRoomUser);
            user.addChatUser(chatRoomUser);
            room.addRoomUser(chatRoomUser);
        }

        chatRoomUserRepository.saveAll(chatRoomUsers);
    }


    public Notification getRoomNotification(Long userId, String roomId) {
        User user = fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);
        ChatRoomUser chatRoomUser = findChatRoomUser(room, user);

        return buildNotificationResponse(chatRoomUser);
    }

    public Title getRoomTitle(String roomId) {
        ChatRoom room = fetchRoom(roomId);
        return RoomResponseMapper.buildTitleResponse(room);
    }

    public RoomDetailResponse.Thumbnail getRoomThumbnail(String roomId) {
        ChatRoom room = fetchRoom(roomId);
        return RoomResponseMapper.buildThumbnailResponse(room);
    }


    public RoomDetailResponse.Host getUserIsRoomHost(Long userId, String roomId) {
        User user = fetchUser(userId);
        ChatRoom room = fetchRoom(roomId);
        ChatRoomUser chatRoomUser = findChatRoomUser(room, user);
        return RoomResponseMapper.buildRoomHostResponse(chatRoomUser);
    }


    private void checkAndDeleteEmptyRoom(ChatRoom room) {
        if (room.getRoomUsers().isEmpty()) {
            chatRoomRepository.deleteById(room.getId());
        }
    }

    private ChatRoomUser findChatRoomUser(ChatRoom room, User user) {
        return chatRoomUserRepository.findByChatRoomAndUser(room, user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND_IN_CHAT_ROOM));
    }

    public User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
    }

    private ChatRoom fetchRoom(String roomId) {
        return chatRoomRepository.findByUuid(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROOM_NOT_FOUND));
    }

    private Meeting fetchMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
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
}
