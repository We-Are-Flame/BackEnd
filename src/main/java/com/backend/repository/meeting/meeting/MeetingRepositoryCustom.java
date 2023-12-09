package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MeetingDetailResponse;
import com.backend.dto.meeting.response.MeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingRepositoryCustom {
    Page<MeetingResponse> findAllWithDetails(Pageable pageable);

    Optional<MeetingDetailResponse> findMeetingWithDetailsById(Long meetingId, Optional<Long> userId);

    List<MyMeetingResponse> findAllByHost(User host);

    List<MyMeetingResponse> findAllParticipatedByUser(User user);

    void deleteMeetingWithAllDetails(Long meetingId);

    boolean isOwner(Long meetingId, Long userId);
}

