package com.backend.repository.chat;

import com.backend.entity.chat.ChatMessage;
import com.backend.entity.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);
    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
