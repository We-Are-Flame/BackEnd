package com.backend.service.meeting;

import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.response.read.MeetingResponse;
import com.backend.dto.meeting.response.read.MeetingResponseList;
import com.backend.dto.meeting.response.read.output.HashtagOutput;
import com.backend.dto.meeting.response.read.output.HostOutput;
import com.backend.dto.meeting.response.read.output.InfoOutput;
import com.backend.dto.meeting.response.read.output.LocationOutput;
import com.backend.dto.meeting.response.read.output.TimeOutput;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingHashtag;
import com.backend.entity.meeting.MeetingImage;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingInfo;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.MeetingMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
        return MeetingMapper.toMeeting(request, category);
    }

    private void processHashtagsAndImages(MeetingCreateRequest request, Meeting meeting) {
        hashtagService.processMeetingHashtags(request.getHashtagInput(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageInput());
    }

    private void createOwnerRegistration(Meeting meeting, User user) {
        registrationService.createOwnerStatus(meeting, user);
    }

    public Page<MeetingResponseList> readMeetings(Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findAllWithDetails(pageable);
        return meetings.map(this::convertToMeetingResponseList);
    }

    private MeetingResponseList convertToMeetingResponseList(Meeting meeting) {
        return MeetingResponseList.builder()
                .responses(meeting.getRegistrations().stream()
                        .map(this::convertToMeetingResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private MeetingResponse convertToMeetingResponse(MeetingRegistration registration) {
        Meeting meeting = registration.getMeeting();
        return MeetingResponse.builder()
                .id(meeting.getId())
                .thumbnailUrl(meeting.findThumbnailImage().map(MeetingImage::getImageUrl).orElse(""))
                .topTags(meeting.findTopHashtags(3)) // 예시로 3개의 태그를 가져옵니다.
                .infoOutput(convertToInfoOutput(meeting))
                .locationOutput(convertToLocationOutput(meeting))
                .timeOutput(convertToTimeOutput(meeting))
                .hostOutput(convertToHostOutput(meeting))
                .build();
    }

    private InfoOutput convertToInfoOutput(Meeting meeting) {
        MeetingInfo info = meeting.getMeetingInfo();
        return InfoOutput.builder()
                .title(info.getTitle())
                .maxParticipants(info.getMaxParticipants())
                .currentParticipants(info.getCurrentParticipants())
                .build();
    }

    private LocationOutput convertToLocationOutput(Meeting meeting) {
        MeetingAddress address = meeting.getMeetingAddress();
        return LocationOutput.builder()
                .location(address.getLocation())
                .detailLocation(address.getDetailLocation())
                .build();
    }

    private TimeOutput convertToTimeOutput(Meeting meeting) {
        MeetingTime time = meeting.getMeetingTime();
        return TimeOutput.builder()
                .startTime(time.getStartTime())
                .endTime(time.getEndTime())
                .duration(time.getDuration())
                .build();
    }

    private HostOutput convertToHostOutput(Meeting meeting) {
        User host = meeting.getHost();
        return HostOutput.builder()
                .name(host.getNickname())
                .profileImage(host.getProfileImage())
                .build();
    }
}
