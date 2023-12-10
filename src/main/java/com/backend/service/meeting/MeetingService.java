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
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.meeting.MeetingRepository;
import com.backend.strategy.CustomSort;
import com.backend.util.mapper.meeting.MeetingRequestMapper;
import java.util.List;
import java.util.Optional;
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

    private static void checkIsKeywordNull(String title, String hashtag) {
        if ((title == null) == (hashtag == null)) {
            throw new BadRequestException(ErrorMessages.DUPLICATE_KEYWORD_TYPE);
        }
    }

    @Transactional
    public Long createMeeting(MeetingCreateRequest request, User user) {
        Meeting meeting = buildMeeting(request, user);
        meetingRepository.save(meeting);

        hashtagService.processMeetingHashtags(request.getHashtagInput(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageInput());
        registrationService.createOwnerStatus(meeting, user);

        return meeting.getId();
    }

    @Transactional(readOnly = true)
    public Page<MeetingResponse> readMeetings(int index, int size, String sort, String categoryName) {
        Pageable pageable = createPagable(index, size, sort);
        Category category = fetchCategory(categoryName);
        return meetingRepository.findAllWithDetails(pageable, category);
    }

    @Transactional(readOnly = true)
    public MeetingDetailResponse readOneMeeting(Long meetingId, User user) {
        MeetingDetailResponse response = fetchMeeting(meetingId, user);
        determineUserStatus(response);
        return response;
    }

    public void determineUserStatus(MeetingDetailResponse response) {
        boolean isOwner = response.getRegistrationRole().isOwner(response.getRegistrationRole());
        boolean isExpired = response.getTimeOutput().isExpired();
        boolean isFull = response.getDetailInfoOutput().isFull();

        StatusOutput statusOutput = StatusOutput.builder()
                .isOwner(isOwner)
                .participateStatus(response.getRegistrationStatus())
                .isExpire(isExpired)
                .isFull(isFull)
                .build();

        response.updateStatus(statusOutput);
    }

    public Page<MeetingResponse> searchByKeword(int index, int size, String sort, String title, String hashtag) {
        Pageable pageable = createPagable(index, size, sort);
        checkIsKeywordNull(title, hashtag);

        if (title != null) {
            return meetingRepository.findByTitle(title, pageable);
        } else {
            return meetingRepository.findByHashtag(hashtag, pageable);
        }
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

    private Pageable createPagable(int index, int size, String sort) {
        return PageRequest.of(index, size, CustomSort.getSort(sort));
    }

    private Category fetchCategory(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        return categoryService.findCategory(categoryName);
    }

    private MeetingDetailResponse fetchMeeting(Long meetingId, User user) {
        return meetingRepository.findMeetingWithDetailsById(meetingId, Optional.ofNullable(user).map(User::getId))
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }
}
