package com.backend.util.mapper;

import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.request.create.input.ImageInput;
import com.backend.dto.meeting.request.create.input.InfoInput;
import com.backend.dto.meeting.request.create.input.LocationInput;
import com.backend.dto.meeting.request.create.input.TimeInput;
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
        InfoInput infoInput = request.getInfoInput();
        LocationInput locationInput = request.getLocationInput();
        TimeInput timeInput = request.getTimeInput();

        MeetingInfo meetingInfo = MeetingMapper.toMeetingInfo(infoInput);
        MeetingAddress meetingAddress = MeetingMapper.toMeetingAddress(locationInput);
        MeetingTime meetingTime = MeetingMapper.toMeetingTime(timeInput);

        return Meeting.builder()
                .meetingInfo(meetingInfo)
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .category(category)
                .build();
    }

    private static MeetingInfo toMeetingInfo(InfoInput infoInput) {
        return MeetingInfo.builder()
                .title(infoInput.getTitle())
                .maxParticipants(infoInput.getMaxParticipants())
                .description(infoInput.getDescription())
                .build();
    }

    private static MeetingTime toMeetingTime(TimeInput timeInput) {
        return MeetingTime.builder()
                .startTime(timeInput.getStartTime())
                .endTime(timeInput.getEndTime())
                .build();
    }

    private static MeetingAddress toMeetingAddress(LocationInput locationInput) {
        return MeetingAddress.builder()
                .location(locationInput.getLocation())
                .detailLocation(locationInput.getDetailLocation())
                .build();
    }

    public static List<MeetingImage> toMeetingImages(Meeting meeting, ImageInput imageInput) {
        List<MeetingImage> meetingImages = new ArrayList<>();

        Optional.ofNullable(imageInput.getThumbnailUrl())
                .ifPresent(url -> meetingImages.add(createMeetingImage(meeting, url, true)));

        Optional.ofNullable(imageInput.getImageUrls())
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
