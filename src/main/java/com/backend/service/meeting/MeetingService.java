package com.backend.service.meeting;

import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingDetailResponse;
import com.backend.dto.meeting.response.MeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponseList;
import com.backend.dto.meeting.response.output.StatusOutput;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.meeting.CustomSort;
import com.backend.repository.meeting.meeting.MeetingRepository;
import com.backend.util.mapper.meeting.MeetingRequestMapper;
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
        Meeting meeting = buildMeeting(request, user);
        meetingRepository.save(meeting);
        handleMeetingDetails(request, meeting);
        registerOwner(meeting, user);
        return meeting.getId();
    }

    @Transactional(readOnly = true)
    public Page<MeetingResponse> readMeetings(int index, int size, String sort) {
        Pageable pageable = PageRequest.of(index, size, CustomSort.getSort(sort));
        return meetingRepository.findAllWithDetails(pageable);
    }

    @Transactional(readOnly = true)
    public MeetingDetailResponse readOneMeeting(Long meetingId, User user) {
        return fetchMeeting(meetingId);
    }

    @Transactional(readOnly = true)
    public MyMeetingResponseList readMyMeetings(User user) {
        List<MyMeetingResponse> participatedMeetings = meetingRepository.findAllByHost(user);
        participatedMeetings.forEach(meeting -> meeting.assignTimeStatus(meeting.getTimeOutput()));

        return new MyMeetingResponseList(participatedMeetings, participatedMeetings.size());
    }

    @Transactional(readOnly = true)
    public MyMeetingResponseList readParticipatedMeetings(User user) {
        List<MyMeetingResponse> participatedMeetings = meetingRepository.findAllParticipatedByUser(user);
        participatedMeetings.forEach(meeting -> meeting.assignTimeStatus(meeting.getTimeOutput()));

        return new MyMeetingResponseList(participatedMeetings, participatedMeetings.size());
    }

    @Transactional
    public Long deleteMeeting(Long meetingId) {
        meetingRepository.deleteMeetingWithAllDetails(meetingId);
        return meetingId;
    }

    private Meeting buildMeeting(MeetingCreateRequest request, User user) {
        Category category = categoryService.findCategory(request.getCategory());
        return MeetingRequestMapper.toMeeting(request, category, user);
    }

    private void handleMeetingDetails(MeetingCreateRequest request, Meeting meeting) {
        hashtagService.processMeetingHashtags(request.getHashtagInput(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageInput());
    }

    private void registerOwner(Meeting meeting, User user) {
        registrationService.createOwnerStatus(meeting, user);
    }

    private MeetingDetailResponse fetchMeeting(Long meetingId) {
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
