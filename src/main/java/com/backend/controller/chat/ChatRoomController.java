package com.backend.controller.chat;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.chat.request.create.RoomCreateRequest;
import com.backend.dto.chat.response.create.ChatRoomUserEnterResponse;
import com.backend.dto.chat.response.delete.ChatRoomUserExitResponse;
import com.backend.dto.chat.response.delete.RoomDeleteResponse;
import com.backend.dto.chat.response.read.ChatResponseList;
import com.backend.dto.chat.response.read.ChatUserResponseList;
import com.backend.dto.chat.response.create.RoomCreateResponse;
import com.backend.dto.chat.response.read.RoomResponseList;
import com.backend.service.chat.ChatService;
import com.backend.service.chat.RoomService;
import com.backend.entity.user.User;
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
    public ResponseEntity<RoomResponseList> getMyChatRooms(@CurrentMember User user){
        RoomResponseList myChatRooms = roomService.getMyChatRooms(user.getId());
        return ResponseEntity.ok(myChatRooms);
    }

    @PostMapping("/room")
    @CheckUserNotNull
    public ResponseEntity<RoomCreateResponse> createRoom(@RequestBody RoomCreateRequest request, @CurrentMember User user) {
        String id = roomService.createChatRoom(request, user.getId());
        RoomCreateResponse response = RoomCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{roomId}")
    @CheckUserNotNull
    public ResponseEntity<ChatRoomUserEnterResponse> enterUserInChatRoom(@CurrentMember User user,
                                                                          @PathVariable String roomId) {
        Long id = roomService.addUserInChatRoom(user.getId(), roomId);
        ChatRoomUserEnterResponse response = ChatRoomUserEnterResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{roomId}")
    @CheckUserNotNull
    public ResponseEntity<RoomDeleteResponse> deleteChatRoom(@CurrentMember User user,
                                                             @PathVariable String roomId) {
        Long id = roomService.deleteChatRoom(user.getId(), roomId);
        RoomDeleteResponse response = RoomDeleteResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{roomId}/user")
    @CheckUserNotNull
    public ResponseEntity<ChatRoomUserExitResponse> exitUserFromChatRoom(@CurrentMember User user,
                                                                         @PathVariable String roomId) {
        Long id = roomService.exitUserFromChatRoom(user.getId(), roomId);
        ChatRoomUserExitResponse response = ChatRoomUserExitResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/users")
    @ResponseBody
    public ResponseEntity<ChatUserResponseList> getUserList(@PathVariable String roomId) {
        ChatUserResponseList roomUsers = roomService.getRoomUserList(roomId);
        return ResponseEntity.ok(roomUsers);
    }

    @GetMapping("/{roomId}/messages")
    @ResponseBody
    public ResponseEntity<ChatResponseList> getMessages(@PathVariable String roomId){
        ChatResponseList roomMessages = chatService.findRoomMessages(roomId);
        return ResponseEntity.ok(roomMessages);
    }


}