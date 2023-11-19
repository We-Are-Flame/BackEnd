package com.backend.service;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.CategoryRepository;
import com.backend.repository.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Meeting createMeeting(MeetingCreateRequest request) {
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));

        Meeting meeting = buildMeeting(request, category);
        return meetingRepository.save(meeting);
    }

    private static Meeting buildMeeting(MeetingCreateRequest request, Category category) {
        return Meeting.builder()
                .name(request.getName())
                .maxParticipants(request.getMaxParticipants())
                .description(request.getDescription())
                .location(request.getLocation())
                .detailLocation(request.getDetailLocation())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .category(category)
                .build();
    }
}
