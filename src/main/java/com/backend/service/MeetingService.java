package com.backend.service;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.CategoryRepository;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final CategoryRepository categoryRepository;
    private final MeetingImagesService meetingImagesService;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request) {
        Category category = findCategory(request.getCategory());
        Meeting meeting = buildAndSaveMeeting(request, category);
        meetingImagesService.saveMeetingImages(meeting, request.getImage());

        return meeting.getId();
    }

    private Category findCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));
    }

    private Meeting buildAndSaveMeeting(MeetingCreateRequest request, Category category) {
        MeetingAddress meetingAddress = MeetingMapper.toMeetingAddress(request.getLocation());
        MeetingTime meetingTime = MeetingMapper.toMeetingTime(request.getTime());

        Meeting meeting = Meeting.builder()
                .name(request.getName())
                .maxParticipants(request.getMaxParticipants())
                .description(request.getDescription())
                .category(category)
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .build();

        return meetingRepository.save(meeting);
    }
}
