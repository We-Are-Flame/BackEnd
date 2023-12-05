package com.backend.service.meeting;

import com.backend.dto.meeting.request.create.input.HashtagInput;
import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingHashtag;
import com.backend.repository.meeting.hashtag.HashtagRepository;
import com.backend.repository.meeting.hashtag.MeetingHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final MeetingHashtagRepository meetingHashtagRepository;

    @Transactional
    public void processMeetingHashtags(HashtagInput hashtagInput, Meeting meeting) {
        hashtagInput.getHashtags().forEach(hashtagName ->
                processHashtag(hashtagName, meeting));
    }

    private void processHashtag(String hashtagName, Meeting meeting) {
        Hashtag hashtag = findOrCreateHashtag(hashtagName);
        linkHashtagToMeeting(hashtag, meeting);
    }

    private Hashtag findOrCreateHashtag(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> createAndSaveHashtag(name));
    }

    private void linkHashtagToMeeting(Hashtag hashtag, Meeting meeting) {
        MeetingHashtag meetingHashtag = MeetingHashtag.builder()
                .hashtag(hashtag)
                .meeting(meeting)
                .build();
        meetingHashtagRepository.save(meetingHashtag);
    }

    private Hashtag createAndSaveHashtag(String name) {
        Hashtag newHashtag = Hashtag.builder()
                .name(name)
                .build();
        return hashtagRepository.save(newHashtag);
    }
}
