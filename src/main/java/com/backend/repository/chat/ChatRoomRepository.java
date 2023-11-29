package com.backend.repository.chat;

import com.backend.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository  extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUuid(String roomId);
    void deleteByUuid(String roomId);
}
