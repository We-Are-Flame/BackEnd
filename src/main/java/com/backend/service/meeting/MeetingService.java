package com.backend.service.meeting;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.embeddable.MeetingAddress;
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
        //요청으로 들어온 카테코리가 존재하는지 확인하고, 있으면 카테고리 객체 반환
        Category category = categoryService.findCategory(request.getCategory());

        //모임 정보 생성
        Meeting meeting = buildAndSaveMeeting(request, category);

        //모임에 쓰인 이미지 저장
        meetingImagesService.saveMeetingImages(meeting, request.getImage());

        //사용자의 등록정보를 "OWNER"로 등록
        meetingRegistrationService.createOwnerStatus(meeting, user);

        return meeting.getId();
    }

    private Meeting buildAndSaveMeeting(MeetingCreateRequest request, Category category) {
        Meeting meeting = buildMeeting(request, category);
        return meetingRepository.save(meeting);
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
}
