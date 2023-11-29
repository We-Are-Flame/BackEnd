package com.backend.entity.chat;

import com.backend.dto.chat.request.create.ChatCreateRequest;
import com.backend.entity.base.BaseEntity;
import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String message;

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    public static ChatMessage of(ChatCreateRequest chatDto, ChatRoom chatRoom, User sender){
        return ChatMessage.builder()
                .messageType(chatDto.getMessageType())
                .chatRoom(chatRoom)
                .sender(sender)
                .message(chatDto.getMessage())
                .build();
    }

}
