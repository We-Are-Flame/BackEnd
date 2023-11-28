package com.backend.util.mapper.meeting;

import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.request.create.input.MeetingImageInput;
import com.backend.dto.meeting.request.create.input.InfoInput;
import com.backend.dto.meeting.request.create.input.LocationInput;
import com.backend.dto.meeting.request.create.input.TimeInput;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MeetingRequestMapper {

    public static Meeting toMeeting(MeetingCreateRequest request, Category category, User user) {
        InfoInput infoInput = request.getInfoInput();
        LocationInput locationInput = request.getLocationInput();
        TimeInput timeInput = request.getTimeInput();

        MeetingAddress meetingAddress = buildLocation(locationInput);
        MeetingTime meetingTime = buildTime(timeInput);

        return Meeting.builder()
                .title(infoInput.getTitle())
                .description(infoInput.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .maxParticipants(infoInput.getMaxParticipants())
                .meetingAddress(meetingAddress)
                .meetingTime(meetingTime)
                .category(category)
                .host(user)
                .build();
    }

    private static MeetingTime buildTime(TimeInput timeInput) {
        return MeetingTime.builder()
                .startTime(timeInput.getStartTime())
                .endTime(timeInput.getEndTime())
                .build();
    }

    private static MeetingAddress buildLocation(LocationInput locationInput) {
        return MeetingAddress.builder()
                .location(locationInput.getLocation())
                .detailLocation(locationInput.getDetailLocation())
                .latitude(locationInput.getLatitude())
                .longitude(locationInput.getLongitude())
                .build();
    }

    public static List<MeetingImage> toMeetingImages(Meeting meeting, MeetingImageInput imageInput) {
        return Optional.ofNullable(imageInput.getImageUrls())
                .orElse(Collections.emptyList())
                .stream()
                .map(url -> buildImage(meeting, url))
                .toList();
    }

    private static MeetingImage buildImage(Meeting meeting, String imageUrl) {
        return MeetingImage.builder()
                .imageUrl(imageUrl)
                .meeting(meeting)
                .build();
    }
}

