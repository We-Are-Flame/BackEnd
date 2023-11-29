package com.backend.repository.chat;

import com.backend.entity.chat.ChatRoom;
import com.backend.entity.chat.ChatRoomUser;
import com.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository  extends JpaRepository<ChatRoomUser, Long> {
    void deleteByChatRoomAndUser(ChatRoom chatRoom, User user);

    List<ChatRoomUser> findAllByChatRoom(ChatRoom chatRoom);

    List<ChatRoomUser> findAllByUser(User user);

    Optional<ChatRoomUser> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatRoomUser> findByChatRoomAndUser(ChatRoom room, User user);
}
