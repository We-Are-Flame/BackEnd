package com.backend.service.meeting;

import com.backend.dto.meeting.common.ImageDTO;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.repository.meeting.MeetingImageRepository;
import com.backend.util.mapper.MeetingMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingImagesService {
    private final MeetingImageRepository meetingImageRepository;

    @Transactional
    public void saveMeetingImages(Meeting meeting, ImageDTO imageDTO) {
        if (imageDTO.NotExistThumbnail()) {
            throw new BadRequestException(ErrorMessages.THUMBNAIL_NOT_EXIST);
        }

        List<MeetingImage> meetingImages = MeetingMapper.toMeetingImages(meeting, imageDTO);
        meetingImageRepository.saveAll(meetingImages);
    }
}
