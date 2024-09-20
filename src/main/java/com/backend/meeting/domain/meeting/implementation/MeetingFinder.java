package com.backend.meeting.domain.meeting.implementation;

import com.backend.before.dto.meeting.response.MeetingDetailResponse;
import com.backend.before.dto.meeting.response.MeetingResponse;
import com.backend.before.entity.user.User;
import com.backend.before.exception.BadRequestException;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.meeting.common.type.FilterCriteria;
import com.backend.meeting.common.type.FilteringType;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MeetingFinder {
    private final MeetingRepository meetingRepository;

    public MeetingDetailResponse findMeetingWithDetailsById(Long meetingId, User user) {
        return meetingRepository.findMeetingWithDetailsById(meetingId, Optional.ofNullable(user).map(User::getId))
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    public Page<MeetingResponse> findByKeyword(String title, String hashtag, Pageable pageable) {
        FilterCriteria criteria = FilteringType.determineFilterCriteria(title, hashtag);
        return meetingRepository.findMeetingsWithFilter(criteria.type(), criteria.value(), pageable);
    }
}