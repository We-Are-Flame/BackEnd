package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingRepositoryCustom {
    Page<Meeting> findAllWithDetails(Pageable pageable);

    Optional<Meeting> findMeetingWithDetailsById(Long id);

    List<MyMeetingResponse> findAllByHost(User host);

    List<MyMeetingResponse> findAllParticipatedByUser(User user);

    void deleteMeetingWithAllDetails(Long meetingId);

    boolean isOwner(Long meetingId, Long userId);
}

