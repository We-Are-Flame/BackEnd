package com.backend.service.meeting;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingInfo;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingImagesService meetingImagesService;
    private final MeetingRegistrationService meetingRegistrationService;
    private final CategoryService categoryService;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request, User user) {
        Category category = categoryService.findCategory(request.getMeetingInfoDTO().getCategory());
        Meeting meeting = toMeeting(request, category);
        meetingRepository.save(meeting);

        meetingImagesService.saveMeetingImages(meeting, request.getImageDTO());
        meetingRegistrationService.createOwnerStatus(meeting, user);

        return meeting.getId();
    }

    private Meeting toMeeting(MeetingCreateRequest request, Category category) {
        MeetingInfo meetingInfo = MeetingMapper.toMeetingInfo(request.getMeetingInfoDTO(), category);
        MeetingAddress meetingAddress = MeetingMapper.toMeetingAddress(request.getLocationDTO());
        MeetingTime meetingTime = MeetingMapper.toMeetingTime(request.getTimeDTO());

        return Meeting.builder()
                .meetingInfo(meetingInfo)
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .build();
    }
}
