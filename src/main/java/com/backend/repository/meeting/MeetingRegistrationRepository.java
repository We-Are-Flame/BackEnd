package com.backend.repository.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRegistrationRepository extends JpaRepository<MeetingRegistration, Long> {
    boolean existsByMeetingAndUser(Meeting meeting, User user);

    Optional<MeetingRegistration> findByMeetingAndUser(Meeting meeting, User user);

    List<MeetingRegistration> findByMeetingAndStatus(Meeting meeting, RegistrationStatus status);
}
