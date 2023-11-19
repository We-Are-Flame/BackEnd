package com.backend.service.meeting;

import com.backend.dto.meeting.dto.LocationDTO;
import com.backend.dto.meeting.dto.MeetingInfoDTO;
import com.backend.dto.meeting.dto.TimeDTO;
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
    private final ImagesService imagesService;
    private final RegistrationService registrationService;
    private final CategoryService categoryService;
    private final HashtagService hashtagService;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request, User user) {
        // 카테고리 탐색 후 Meeting생성과 함께 맵핑, 저장
        Category category = findCategory(request.getCategory());
        Meeting meeting = buildMeeting(request, category);
        meetingRepository.save(meeting);

        //이미지 저장 및 해시태그 생성, 맵핑
        processHashtagsAndImages(request, meeting);

        //Meeting의 생성자를 Owner로 등록
        createOwnerRegistration(meeting, user);

        return meeting.getId();
    }

    private Category findCategory(String categoryName) {
        return categoryService.findCategory(categoryName);
    }

    private Meeting buildMeeting(MeetingCreateRequest request, Category category) {
        MeetingInfoDTO meetingInfoDTO = request.getMeetingInfoDTO();
        LocationDTO locationDTO = request.getLocationDTO();
        TimeDTO timeDTO = request.getTimeDTO();

        MeetingInfo meetingInfo = MeetingMapper.toMeetingInfo(meetingInfoDTO);
        MeetingAddress meetingAddress = MeetingMapper.toMeetingAddress(locationDTO);
        MeetingTime meetingTime = MeetingMapper.toMeetingTime(timeDTO);

        return Meeting.builder()
                .meetingInfo(meetingInfo)
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .category(category)
                .build();
    }

    private void processHashtagsAndImages(MeetingCreateRequest request, Meeting meeting) {
        hashtagService.processMeetingHashtags(request.getHashtagDTO(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageDTO());
    }

    private void createOwnerRegistration(Meeting meeting, User user) {
        registrationService.createOwnerStatus(meeting, user);
    }
}
