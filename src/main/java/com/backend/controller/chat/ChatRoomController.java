package com.backend.controller.chat;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.chat.request.create.RoomCreateRequest;
import com.backend.dto.chat.response.delete.RoomDeleteResponse;
import com.backend.dto.chat.response.read.ChatResponseList;
import com.backend.dto.chat.response.read.ChatUserResponseList;
import com.backend.dto.chat.response.create.RoomCreateResponse;
import com.backend.dto.chat.response.read.RoomResponse;
import com.backend.dto.chat.response.read.RoomResponseList;
import com.backend.service.chat.ChatService;
import com.backend.service.chat.RoomService;
import com.backend.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
        log.info("SHOW ALL ChatList {}", myChatRooms);
        return ResponseEntity.ok(myChatRooms);
    }

    @PostMapping("/room")
    @CheckUserNotNull
    public ResponseEntity<RoomCreateResponse> createRoom(@RequestBody RoomCreateRequest request, @CurrentMember User user) {
        Long id = roomService.createChatRoom(request, user.getId());
        log.info("CREATE Chat Room [{}]", id);
        RoomCreateResponse response = RoomCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{roomId}")
    @CheckUserNotNull
    public ResponseEntity<RoomDeleteResponse> deleteChatRoom(@CurrentMember User user,
                                                             @PathVariable String roomId) {
        Long id = roomService.deleteChatRoom(user, roomId);
        log.info("DELETE Chat Room [{}]", id);
        RoomDeleteResponse response = RoomDeleteResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/messages")
    @ResponseBody
    public ResponseEntity<ChatResponseList> getMessages(@PathVariable String roomId){
        ChatResponseList roomMessages = chatService.findRoomMessages(roomId);
        return ResponseEntity.ok(roomMessages);
    }

    @GetMapping("/{roomId}/users")
    @ResponseBody
    public ResponseEntity<ChatUserResponseList> getUserList(@PathVariable String roomId) {
        ChatUserResponseList roomUsers = roomService.getRoomUserList(roomId);
        return ResponseEntity.ok(roomUsers);
    }
}