package com.backend.controller.chat;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.chat.request.create.RoomCreateRequest;
import com.backend.dto.chat.request.update.RoomUpdateRequest;
import com.backend.dto.chat.response.create.ChatRoomUserEnterResponse;
import com.backend.dto.chat.response.create.RoomCreateResponse;
import com.backend.dto.chat.response.delete.RoomDeleteResponse;
import com.backend.dto.chat.response.delete.RoomUserExitResponse;
import com.backend.dto.chat.response.read.ChatResponseList;
import com.backend.dto.chat.response.read.ChatUserResponseList;
import com.backend.dto.chat.response.read.RoomDetailResponse;
import com.backend.dto.chat.response.read.RoomResponseList;
import com.backend.dto.common.ResponseMessage;
import com.backend.dto.common.SuccessResponse;
import com.backend.entity.user.User;
import com.backend.service.chat.ChatService;
import com.backend.service.chat.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatService chatService;
    private final RoomService roomService;

    @GetMapping("rooms")
    @CheckUserNotNull
    public ResponseEntity<RoomResponseList> getMyChatRooms(@CurrentMember User user) {
        RoomResponseList myChatRooms = roomService.getMyChatRooms(user.getId());
        return ResponseEntity.ok(myChatRooms);
    }

    @PostMapping("/room")
    @CheckUserNotNull
    public ResponseEntity<RoomCreateResponse> createRoom(@RequestBody RoomCreateRequest request,
                                                         @CurrentMember User user) {
        String id = roomService.createChatRoom(request, user.getId());
        RoomCreateResponse response = RoomCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{roomId}/user")
    @CheckUserNotNull
    public ResponseEntity<RoomUserExitResponse> exitUserFromChatRoom(@CurrentMember User user,
                                                                     @PathVariable String roomId) {
        Long id = roomService.exitUserFromChatRoom(user.getId(), roomId);
        RoomUserExitResponse response = RoomUserExitResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/users")
    public ResponseEntity<ChatUserResponseList> getUserList(@PathVariable String roomId) {
        ChatUserResponseList roomUsers = roomService.getRoomUserList(roomId);
        return ResponseEntity.ok(roomUsers);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ChatResponseList> getMessages(@PathVariable String roomId) {
        ChatResponseList roomMessages = chatService.findRoomMessages(roomId);
        return ResponseEntity.ok(roomMessages);
    }

    @GetMapping("/{roomId}/notification")
    @CheckUserNotNull
    public ResponseEntity<RoomDetailResponse.Notification> getChatRoomNotification(@CurrentMember User user,
                                                                                   @PathVariable String roomId) {
        RoomDetailResponse.Notification response = roomService.getRoomNotification(user.getId(), roomId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roomId}/notification")
    @CheckUserNotNull
    public ResponseEntity<SuccessResponse> updateChatRoomNotification(
            @RequestBody RoomUpdateRequest.Notification request,
            @CurrentMember User user,
            @PathVariable String roomId) {
        Long id = roomService.updateRoomNotification(request, user.getId(), roomId);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.CHAT_ROOM_NOTIFICATION_UPDATE_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/title")
    @CheckUserNotNull
    public ResponseEntity<RoomDetailResponse.Title> getChatRoomTitle(@CurrentMember User user,
                                                                     @PathVariable String roomId) {
        RoomDetailResponse.Title response = roomService.getRoomTitle(roomId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roomId}/title")
    @CheckUserNotNull
    public ResponseEntity<SuccessResponse> updateChatRoomNotification(@RequestBody RoomUpdateRequest.Title request,
                                                                      @CurrentMember User user,
                                                                      @PathVariable String roomId) {
        Long id = roomService.updateRoomTitle(request, user.getId(), roomId);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.CHAT_ROOM_TITLE_UPDATE_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/thumbnail")
    @CheckUserNotNull
    public ResponseEntity<RoomDetailResponse.Thumbnail> getChatRoomThumbnail(@CurrentMember User user,
                                                                             @PathVariable String roomId) {
        RoomDetailResponse.Thumbnail response = roomService.getRoomThumbnail(roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/host")
    @CheckUserNotNull
    ResponseEntity<RoomDetailResponse.Host> getUserNotification(@CurrentMember User user,
                                                                @PathVariable String roomId) {
        RoomDetailResponse.Host response = roomService.getUserIsRoomHost(user.getId(), roomId);
        return ResponseEntity.ok(response);
    }

}
