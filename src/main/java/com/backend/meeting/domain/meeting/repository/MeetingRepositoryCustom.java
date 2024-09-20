package com.backend.meeting.domain.meeting.repository;

import com.backend.before.dto.meeting.response.MeetingDetailResponse;
import com.backend.before.dto.meeting.response.MeetingResponse;
import com.backend.before.dto.meeting.response.MyMeetingResponse;
import com.backend.meeting.common.type.FilteringType;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.before.entity.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingRepositoryCustom {
    Page<MeetingResponse> findAllWithDetails(Pageable pageable, Category category);
    Page<MeetingResponse> findMeetingsWithFilter(FilteringType type, String filterValue, Pageable pageable);

    Optional<MeetingDetailResponse> findMeetingWithDetailsById(Long meetingId, Optional<Long> userId);

    List<Meeting> findForEvaluation(LocalDateTime endTime);
    List<MyMeetingResponse> findAllByHost(User host);
    List<MyMeetingResponse> findAllParticipatedByUser(User user);

    boolean isOwner(Long meetingId, Long userId);
    void deleteMeetingWithAllDetails(Long meetingId);
}