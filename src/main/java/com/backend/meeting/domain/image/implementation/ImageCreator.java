package com.backend.meeting.domain.image.implementation;

import com.backend.before.dto.meeting.request.create.input.MeetingImageInput;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.meeting.domain.image.entity.MeetingImage;
import com.backend.meeting.domain.image.repository.MeetingImageRepository;
import com.backend.meeting.common.mapper.MeetingtMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageCreator {
    private final MeetingImageRepository meetingImageRepository;

    @Transactional
    public void saveInMeeting(Meeting meeting, MeetingImageInput imageInput) {
        List<MeetingImage> meetingImages = MeetingtMapper.toMeetingImages(meeting, imageInput);
        meetingImageRepository.saveAll(meetingImages);
    }
}
