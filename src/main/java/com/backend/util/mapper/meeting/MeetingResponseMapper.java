package com.backend.util.mapper.meeting;

import com.backend.dto.meeting.response.read.MeetingDetailResponse;
import com.backend.dto.meeting.response.read.MeetingResponse;
import com.backend.dto.meeting.response.read.MyMeetingResponse;
import com.backend.dto.meeting.response.read.output.DetailInfoOutput;
import com.backend.dto.meeting.response.read.output.DetailTimeOutput;
import com.backend.dto.meeting.response.read.output.HostOutput;
import com.backend.dto.meeting.response.read.output.InfoOutput;
import com.backend.dto.meeting.response.read.output.LocationOutput;
import com.backend.dto.meeting.response.read.output.MeetingImageOutput;
import com.backend.dto.meeting.response.read.output.StatusOutput;
import com.backend.dto.meeting.response.read.output.TimeOutput;
import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingImage;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MeetingResponseMapper {

    public static MeetingResponse toMeetingResponse(Meeting meeting) {
        return MeetingResponse.builder()
                .id(meeting.getId())
                .thumbnailUrl(meeting.getThumbnailUrl())
                .hashtags(extractHashTags(meeting))
                .infoOutput(buildInfo(meeting))
                .locationOutput(buildLocation(meeting.getMeetingAddress()))
                .timeOutput(buildTime(meeting.getMeetingTime()))
                .hostOutput(buildHost(meeting.getHost()))
                .build();
    }

    public static MeetingDetailResponse toMeetingDetailResponse(Meeting meeting, StatusOutput status) {
        return MeetingDetailResponse.builder()
                .id(meeting.getId())
                .hashtags(extractHashTags(meeting))
                .detailInfoOutput(buildDetailInfo(meeting))
                .imageOutput(buildImage(meeting.getMeetingImages(), meeting.getThumbnailUrl()))
                .locationOutput(buildLocation(meeting.getMeetingAddress()))
                .timeOutput(buildDetailTime(meeting.getMeetingTime(), meeting.getCreatedAt()))
                .hostOutput(buildHost(meeting.getHost()))
                .statusOutput(status)
                .build();
    }

    public static MyMeetingResponse toMyMeetingResponse(Meeting meeting) {
        return MyMeetingResponse.builder()
                .id(meeting.getId())
                .thumbnailUrl(meeting.getThumbnailUrl())
                .hashtags(extractHashTags(meeting))
                .infoOutput(buildInfo(meeting))
                .locationOutput(buildLocation(meeting.getMeetingAddress()))
                .timeOutput(buildTime(meeting.getMeetingTime()))
                .build();
    }

    private static List<String> extractHashTags(Meeting meeting) {
        return meeting.getHashtags().stream()
                .map(Hashtag::getName)
                .collect(Collectors.toList());
    }

    private static InfoOutput buildInfo(Meeting meeting) {
        return InfoOutput.builder()
                .title(meeting.getTitle())
                .maxParticipants(meeting.getMaxParticipants())
                .currentParticipants(meeting.getCurrentParticipants())
                .build();
    }

    private static DetailInfoOutput buildDetailInfo(Meeting meeting) {
        return DetailInfoOutput.builder()
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .maxParticipants(meeting.getMaxParticipants())
                .currentParticipants(meeting.getCurrentParticipants())
                .build();
    }

    private static MeetingImageOutput buildImage(Set<MeetingImage> images, String thumbnailUrl) {
        List<String> imageUrls = images.stream()
                .map(MeetingImage::getImageUrl)
                .collect(Collectors.toList());

        return MeetingImageOutput.builder()
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .build();
    }

    private static LocationOutput buildLocation(MeetingAddress address) {
        return LocationOutput.builder()
                .location(address.getLocation())
                .detailLocation(address.getDetailLocation())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }

    private static TimeOutput buildTime(MeetingTime time) {
        return TimeOutput.builder()
                .startTime(time.getStartTime())
                .endTime(time.getEndTime())
                .duration(time.getDuration())
                .build();
    }

    private static DetailTimeOutput buildDetailTime(MeetingTime time, LocalDateTime createdAt) {
        return DetailTimeOutput.builder()
                .startTime(time.getStartTime())
                .endTime(time.getEndTime())
                .createdAt(createdAt)
                .duration(time.getDuration())
                .build();
    }

    private static HostOutput buildHost(User host) {
        return HostOutput.builder()
                .name(host.getNickname())
                .profileImage(host.getProfileImage())
                .build();
    }
}
