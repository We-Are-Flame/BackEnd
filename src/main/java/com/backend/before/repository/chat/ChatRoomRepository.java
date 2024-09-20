package com.backend.before.repository.chat;

import com.backend.before.entity.chat.ChatRoom;
import com.backend.meeting.domain.meeting.entity.Meeting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUuid(String roomId);

    void deleteByUuid(String roomId);

    Optional<ChatRoom> findByMeeting(Meeting meeting);

    Optional<ChatRoom> findByMeetingId(Long meetingId);
}
