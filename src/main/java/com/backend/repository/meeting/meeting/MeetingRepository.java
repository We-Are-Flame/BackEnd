package com.backend.repository.meeting.meeting;

import com.backend.entity.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingRepositoryCustom {
}

