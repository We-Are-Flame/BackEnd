package com.backend.before.repository.chat;

import com.backend.before.entity.chat.ChatMessage;
import com.backend.before.entity.chat.ChatRoom;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);

    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
