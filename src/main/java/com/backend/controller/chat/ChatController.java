package com.backend.controller.chat;

import com.backend.dto.chat.request.create.ChatCreateRequest;
import com.backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatService chatService;

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatCreateRequest chatRequest, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("userId", chatRequest.getSenderId());
        headerAccessor.getSessionAttributes().put("roomId", chatRequest.getRoomId());

    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatCreateRequest chatRequest) {
        template.convertAndSend("/sub/chat/room/" + chatRequest.getRoomId(), chatRequest);
        chatService.saveMessage(chatRequest, chatRequest.getSenderId());
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

    }
}
