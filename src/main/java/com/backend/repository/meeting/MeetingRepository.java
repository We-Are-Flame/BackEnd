package com.backend.repository.meeting;

import com.backend.entity.meeting.Meeting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingRepositoryCustom {
    @Query("SELECT m.host.id FROM Meeting m WHERE m.id = :meetingId")
    Optional<Long> findOwnerIdByMeetingId(@Param("meetingId") Long meetingId);
}

