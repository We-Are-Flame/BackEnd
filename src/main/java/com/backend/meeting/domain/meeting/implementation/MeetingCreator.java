package com.backend.meeting.domain.meeting.implementation;

import com.backend.before.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.before.entity.user.User;
import com.backend.meeting.common.mapper.MeetingtMapper;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingCreator {
    private final MeetingRepository meetingRepository;

    public Meeting create(MeetingCreateRequest request, Category category, User user) {
        Meeting meeting = MeetingtMapper.requestToEntity(request, category, user);
        return meetingRepository.save(meeting);
    }
}
