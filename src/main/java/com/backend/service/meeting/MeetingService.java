package com.backend.service.meeting;

import com.backend.annotation.CheckIsOwner;
import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.response.read.MeetingDetailResponse;
import com.backend.dto.meeting.response.read.MeetingResponse;
import com.backend.dto.meeting.response.read.MyMeetingResponse;
import com.backend.dto.meeting.response.read.MyMeetingResponseList;
import com.backend.dto.meeting.response.read.output.StatusOutput;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.meeting.MeetingRequestMapper;
import com.backend.util.mapper.meeting.MeetingResponseMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ImagesService imagesService;
    private final RegistrationService registrationService;
    private final CategoryService categoryService;
    private final HashtagService hashtagService;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request, User user) {
        Meeting meeting = prepareMeeting(request, user);
        meetingRepository.save(meeting);
        handleMeetingAssets(request, meeting);
        registerOwner(meeting, user);
        return meeting.getId();
    }

    @Transactional(readOnly = true)
    public Page<MeetingResponse> readMeetings(int start, int end, String sort) {
        Pageable pageable = createPageable(start, end, sort);
        Page<Meeting> meetings = meetingRepository.findAllWithDetails(pageable);
        return meetings.map(MeetingResponseMapper::toMeetingResponse);
    }

    @Transactional(readOnly = true)
    public MeetingDetailResponse readOneMeeting(Long meetingId, User user) {
        Meeting meeting = fetchMeeting(meetingId);
        StatusOutput status = buildMeetingStatus(meeting, user);
        return MeetingResponseMapper.toMeetingDetailResponse(meeting, status);
    }

    @Transactional(readOnly = true)
    public MyMeetingResponseList readMyMeetings(User user) {
        List<Meeting> myMeetings = meetingRepository.findAllByHost(user);
        List<MyMeetingResponse> myMeetingResponses = myMeetings.stream()
                .map(MeetingResponseMapper::toMyMeetingResponse)
                .toList();
        return new MyMeetingResponseList(myMeetingResponses, myMeetingResponses.size());
    }

    @CheckIsOwner
    @Transactional
    public void deleteMeeting(Long meetingId, User user) {
        meetingRepository.deleteMeetingWithAllDetails(meetingId);
    }

    // Private helper methods
    private Meeting prepareMeeting(MeetingCreateRequest request, User user) {
        Category category = categoryService.findCategory(request.getCategory());
        return MeetingRequestMapper.toMeeting(request, category, user);
    }

    private void handleMeetingAssets(MeetingCreateRequest request, Meeting meeting) {
        hashtagService.processMeetingHashtags(request.getHashtagInput(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageInput());
    }

    private void registerOwner(Meeting meeting, User user) {
        registrationService.createOwnerStatus(meeting, user);
    }

    private Pageable createPageable(int start, int end, String sort) {
        int page = start / end;
        return PageRequest.of(page, end, CustomSort.getSort(sort));
    }

    private Meeting fetchMeeting(Long meetingId) {
        return meetingRepository.findMeetingWithDetailsById(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private StatusOutput buildMeetingStatus(Meeting meeting, User user) {
        return StatusOutput.builder()
                .isOwner(meeting.isUserOwner(user))
                .participateStatus(meeting.determineParticipationStatus(user))
                .isExpire(meeting.isExpired())
                .build();
    }
}
