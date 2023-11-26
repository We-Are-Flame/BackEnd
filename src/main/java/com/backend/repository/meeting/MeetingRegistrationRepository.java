package com.backend.repository.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRegistrationRepository extends JpaRepository<MeetingRegistration, Long> {
    boolean existsByMeetingAndUser(Meeting meeting, User user);
}
