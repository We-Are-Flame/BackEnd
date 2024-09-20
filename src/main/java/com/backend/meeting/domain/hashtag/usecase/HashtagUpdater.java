package com.backend.meeting.domain.hashtag.usecase;

import com.backend.meeting.domain.hashtag.entity.Hashtag;
import com.backend.meeting.domain.hashtag.entity.MeetingHashtag;
import com.backend.meeting.domain.hashtag.repository.MeetingHashtagRepository;
import com.backend.meeting.domain.meeting.entity.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class HashtagUpdater {
    private final MeetingHashtagRepository meetingHashtagRepository;

    @Transactional
    public void linkToMeeting(Hashtag hashtag, Meeting meeting) {
        MeetingHashtag meetingHashtag = MeetingHashtag.builder()
                .hashtag(hashtag)
                .meeting(meeting)
                .build();
        meetingHashtagRepository.save(meetingHashtag);
    }
}