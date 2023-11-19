package com.backend.repository.meeting;

import com.backend.entity.meeting.MeetingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRegistrationRepository extends JpaRepository<MeetingRegistration, Long> {
}
