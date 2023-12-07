package com.backend.repository.chat;

import com.backend.entity.chat.ChatRoom;
import com.backend.entity.meeting.Meeting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUuid(String roomId);

    void deleteByUuid(String roomId);

    Optional<ChatRoom> findByMeeting(Meeting meeting);

    Optional<ChatRoom> findByMeetingId(Long meetingId);
}
