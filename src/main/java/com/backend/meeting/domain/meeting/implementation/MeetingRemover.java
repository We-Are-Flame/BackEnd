package com.backend.meeting.domain.meeting.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingRemover {
    private final MeetingRepository meetingRepository;

    @Transactional
    public void deleteMeetingWithAllDetails(Long meetingId) {
        meetingRepository.deleteMeetingWithAllDetails(meetingId);
    }
}
