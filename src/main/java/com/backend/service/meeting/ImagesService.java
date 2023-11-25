package com.backend.service.meeting;

import com.backend.dto.meeting.request.create.input.ImageInput;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.repository.meeting.MeetingImageRepository;
import com.backend.util.mapper.meeting.MeetingRequestMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final MeetingImageRepository meetingImageRepository;

    @Transactional
    public void saveMeetingImages(Meeting meeting, ImageInput imageInput) {
        List<MeetingImage> meetingImages = MeetingRequestMapper.toMeetingImages(meeting, imageInput);
        meetingImageRepository.saveAll(meetingImages);
    }
}
