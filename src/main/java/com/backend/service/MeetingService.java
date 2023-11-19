package com.backend.service;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.CategoryRepository;
import com.backend.repository.meeting.MeetingImageRepository;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.MeetingMapper;
import com.backend.util.mock.UserMocking;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final CategoryRepository categoryRepository;
    private final MeetingImageRepository meetingImageRepository;
    private final UserMocking userMocking;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request) {
        Category category = getCategoryByName(request.getCategory());
        Meeting meeting = saveMeeting(request, category);
        saveMeetingImages(request, meeting);

        return meeting.getId();
    }

    private Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));
    }

    private Meeting saveMeeting(MeetingCreateRequest request, Category category) {
        Meeting meeting = buildMeeting(request, category);
        meetingRepository.save(meeting);
        return meeting;
    }

    private Meeting buildMeeting(MeetingCreateRequest request, Category category) {
        MeetingAddress meetingAddress = MeetingMapper.toMeetingAddress(request.getLocation());
        MeetingTime meetingTime = MeetingMapper.toMeetingTime(request.getTime());

        return Meeting.builder()
                .name(request.getName())
                .maxParticipants(request.getMaxParticipants())
                .description(request.getDescription())
                .category(category)
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .build();
    }

    private void saveMeetingImages(MeetingCreateRequest request, Meeting meeting) {
        if (!request.hasThumbnail()) {
            throw new BadRequestException(ErrorMessages.THUMBNAIL_NOT_EXIST);
        }

        List<MeetingImage> meetingImages = MeetingMapper.toMeetingImages(meeting, request.getImage());
        meetingImageRepository.saveAll(meetingImages);
    }
}
