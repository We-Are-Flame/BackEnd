package com.backend.meeting.service;

import com.backend.before.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.before.dto.meeting.response.MeetingDetailResponse;
import com.backend.before.dto.meeting.response.MeetingResponse;
import com.backend.before.dto.meeting.response.MyMeetingResponse;
import com.backend.before.dto.meeting.response.MyMeetingResponseList;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.hashtag.entity.Hashtag;
import com.backend.meeting.domain.hashtag.usecase.HashtagCreator;
import com.backend.meeting.domain.hashtag.usecase.HashtagUpdater;
import com.backend.meeting.domain.image.implementation.ImageCreator;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.before.entity.user.User;
import com.backend.meeting.domain.meeting.implementation.MeetingCreator;
import com.backend.meeting.domain.meeting.implementation.MeetingFinder;
import com.backend.meeting.domain.meeting.implementation.MeetingRemover;
import com.backend.meeting.domain.meeting.implementation.MeetingReader;
import com.backend.meeting.common.type.SortType;

import java.util.List;

import com.backend.meeting.domain.category.implemantation.CategorySearcher;
import com.backend.registration.implemantation.RegistrationCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingCreator meetingCreator;
    private final MeetingReader meetingReader;
    private final MeetingFinder meetingFinder;
    private final MeetingRemover meetingRemover;
    private final ImageCreator imageCreator;
    private final HashtagCreator hashtagCreator;
    private final HashtagUpdater hashtagUpdater;
    private final CategorySearcher categorySearcher;
    private final RegistrationCreator registrationCreator;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request, User user) {
        Category category = categorySearcher.findByName(request.getCategory());
        Meeting meeting = meetingCreator.create(request, category, user);

        request.getHashtags().forEach(hashtagName -> {
            Hashtag hashtag = hashtagCreator.findOrCreate(hashtagName);
            hashtagUpdater.linkToMeeting(hashtag, meeting);
        });

        imageCreator.saveInMeeting(meeting, request.getImageInput());
        registrationCreator.createOwnerStatus(meeting, user);

        return meeting.getId();
    }

    @Transactional(readOnly = true)
    public Page<MeetingResponse> readMeetings(int index, int size, String sort, String categoryName) {
        Pageable pageable = PageRequest.of(index, size, SortType.getSort(sort));
        Category category = categorySearcher.findByName(categoryName);
        return meetingReader.readAllWithDetails(pageable, category);
    }

    @Transactional(readOnly = true)
    public MeetingDetailResponse readOneMeeting(Long meetingId, User user) {
        MeetingDetailResponse response = meetingFinder.findMeetingWithDetailsById(meetingId, user);
        return response.updateStatus();
    }

    public Page<MeetingResponse> readMeetingByKeyword(int index, int size, String sort, String title, String hashtag) {
        Pageable pageable = PageRequest.of(index, size, SortType.getSort(sort));
        return meetingFinder.findByKeyword(title, hashtag, pageable);
    }

    @Transactional(readOnly = true)
    public MyMeetingResponseList readMyMeetings(User user) {
        List<MyMeetingResponse> hostedMeetings = meetingReader.findAllByHost(user);
        return new MyMeetingResponseList(hostedMeetings, hostedMeetings.size());
    }

    @Transactional(readOnly = true)
    public MyMeetingResponseList readParticipatedMeetings(User user) {
        List<MyMeetingResponse> participatedMeetings = meetingReader.findAllParticipatedByUser(user);
        return new MyMeetingResponseList(participatedMeetings, participatedMeetings.size());
    }

    @Transactional
    public Long deleteMeeting(Long meetingId) {
        meetingRemover.deleteMeetingWithAllDetails(meetingId);
        return meetingId;
    }
}