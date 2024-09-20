package com.backend.meeting.domain.meeting.implementation;

import com.backend.before.dto.meeting.response.MeetingResponse;
import com.backend.before.dto.meeting.response.MyMeetingResponse;
import com.backend.before.entity.user.User;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingReader {
    private final MeetingRepository meetingRepository;

    public Page<MeetingResponse> readAllWithDetails(Pageable pageable, Category category) {
        return meetingRepository.findAllWithDetails(pageable, category);
    }

    public List<MyMeetingResponse> findAllByHost(User user) {
        List<MyMeetingResponse> hostedMeetings = meetingRepository.findAllByHost(user);
        hostedMeetings.forEach(meeting -> meeting.assignTimeStatus(meeting.getTimeOutput()));
        return hostedMeetings;
    }

    public List<MyMeetingResponse> findAllParticipatedByUser(User user) {
        List<MyMeetingResponse> participatedMeetings = meetingRepository.findAllParticipatedByUser(user);
        participatedMeetings.forEach(meeting -> meeting.assignTimeStatus(meeting.getTimeOutput()));
        return participatedMeetings;
    }
}