package com.backend.service.meeting;

import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.response.read.MeetingDetailResponse;
import com.backend.dto.meeting.response.read.MeetingResponse;
import com.backend.dto.meeting.response.read.output.DetailInfoOutput;
import com.backend.dto.meeting.response.read.output.DetailTimeOutput;
import com.backend.dto.meeting.response.read.output.HostOutput;
import com.backend.dto.meeting.response.read.output.ImageOutput;
import com.backend.dto.meeting.response.read.output.LocationOutput;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.entity.user.User;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.meeting.MeetingRequestMapper;
import com.backend.util.mapper.meeting.MeetingResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Meeting meeting = buildMeeting(request, category, user);
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

    private Meeting buildMeeting(MeetingCreateRequest request, Category category, User user) {
        return MeetingRequestMapper.toMeeting(request, category, user);
    }

    private void processHashtagsAndImages(MeetingCreateRequest request, Meeting meeting) {
        hashtagService.processMeetingHashtags(request.getHashtagInput(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageInput());
    }

    private void createOwnerRegistration(Meeting meeting, User user) {
        registrationService.createOwnerStatus(meeting, user);
    }

    public Page<MeetingResponse> readMeetings(int start, int end, String sort) {
        int page = start / end;
        Pageable pageable = PageRequest.of(page, end, CustomSort.getSort(sort));
        Page<Meeting> meetings = meetingRepository.findAllWithDetails(pageable);
        return meetings.map(MeetingResponseMapper::toMeetingResponse);
    }

    public MeetingDetailResponse readOneMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findMeetingWithDetailsById(meetingId)
                .orElseThrow(() -> new NotFoundException("PK에 맞는 모임이 없습니다."));
        return convertToMeetingDetailResponse(meeting);
    }

    private MeetingDetailResponse convertToMeetingDetailResponse(Meeting meeting) {
        List<String> hashtags = meeting.getHashtags().stream()
                .map(Hashtag::getName)
                .collect(Collectors.toList());

        DetailInfoOutput infoOutput = DetailInfoOutput.builder()
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .maxParticipants(meeting.getMaxParticipants())
                .currentParticipants(meeting.getCurrentParticipants())
                .build();

        ImageOutput imageOutput = ImageOutput.builder()
                .thumbnailUrl(meeting.getThumbnailUrl())
                .imageUrls(meeting.getMeetingImages().stream()
                        .map(MeetingImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();

        LocationOutput locationOutput = LocationOutput.builder()
                .location(meeting.getMeetingAddress().getLocation())
                .detailLocation(meeting.getMeetingAddress().getDetailLocation())
                .build();

        DetailTimeOutput timeOutput = DetailTimeOutput.builder()
                .startTime(meeting.getMeetingTime().getStartTime())
                .endTime(meeting.getMeetingTime().getEndTime())
                .createdAt(meeting.getCreatedAt())
                .duration(meeting.getMeetingTime().getDuration())
                .build();

        HostOutput hostOutput = HostOutput.builder()
                .name(meeting.getHost().getNickname())
                .profileImage(meeting.getHost().getProfileImage())
                .build();

        return MeetingDetailResponse.builder()
                .id(meeting.getId())
                .hashtags(hashtags)
                .detailInfoOutput(infoOutput)
                .imageOutput(imageOutput)
                .locationOutput(locationOutput)
                .timeOutput(timeOutput)
                .hostOutput(hostOutput)
                ///TODO [HJ] 모임에 대한 상태 로직 판별부터 구현
                .build();
    }
}
