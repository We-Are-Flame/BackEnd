package com.backend.util.mapper;

import com.backend.dto.meeting.dto.ImageDTO;
import com.backend.dto.meeting.dto.LocationDTO;
import com.backend.dto.meeting.dto.MeetingInfoDTO;
import com.backend.dto.meeting.dto.TimeDTO;
import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingInfo;
import com.backend.entity.meeting.embeddable.MeetingTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MeetingMapper {
    public static Meeting toMeeting(MeetingCreateRequest request, Category category) {
        MeetingInfoDTO meetingInfoDTO = request.getMeetingInfoDTO();
        LocationDTO locationDTO = request.getLocationDTO();
        TimeDTO timeDTO = request.getTimeDTO();

        MeetingInfo meetingInfo = MeetingMapper.toMeetingInfo(meetingInfoDTO);
        MeetingAddress meetingAddress = MeetingMapper.toMeetingAddress(locationDTO);
        MeetingTime meetingTime = MeetingMapper.toMeetingTime(timeDTO);

        return Meeting.builder()
                .meetingInfo(meetingInfo)
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .category(category)
                .build();
    }

    private static MeetingInfo toMeetingInfo(MeetingInfoDTO meetingInfoDTO) {
        return MeetingInfo.builder()
                .title(meetingInfoDTO.getTitle())
                .maxParticipants(meetingInfoDTO.getMaxParticipants())
                .description(meetingInfoDTO.getDescription())
                .build();
    }

    private static MeetingTime toMeetingTime(TimeDTO timeDTO) {
        return MeetingTime.builder()
                .startTime(timeDTO.getStartTime())
                .endTime(timeDTO.getEndTime())
                .build();
    }

    private static MeetingAddress toMeetingAddress(LocationDTO locationDTO) {
        return MeetingAddress.builder()
                .location(locationDTO.getLocation())
                .detailLocation(locationDTO.getDetailLocation())
                .build();
    }

    public static List<MeetingImage> toMeetingImages(Meeting meeting, ImageDTO imageDTO) {
        List<MeetingImage> meetingImages = new ArrayList<>();

        Optional.ofNullable(imageDTO.getThumbnailUrl())
                .ifPresent(url -> meetingImages.add(createMeetingImage(meeting, url, true)));

        Optional.ofNullable(imageDTO.getImageUrls())
                .ifPresent(urls -> urls.forEach(url ->
                        meetingImages.add(createMeetingImage(meeting, url, false))));

        return meetingImages;
    }

    private static MeetingImage createMeetingImage(Meeting meeting, String imageUrl, boolean isThumbnail) {
        return MeetingImage.builder()
                .imageUrl(imageUrl)
                .isThumbnail(isThumbnail)
                .meeting(meeting)
                .build();
    }
}
