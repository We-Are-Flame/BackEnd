package com.backend.util.mapper.meeting;

import com.backend.dto.meeting.response.read.MeetingResponse;
import com.backend.dto.meeting.response.read.output.HostOutput;
import com.backend.dto.meeting.response.read.output.InfoOutput;
import com.backend.dto.meeting.response.read.output.LocationOutput;
import com.backend.dto.meeting.response.read.output.TimeOutput;
import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
import java.util.List;
import java.util.stream.Collectors;

public class MeetingResponseMapper {
    public static MeetingResponse toMeetingResponse(Meeting meeting) {
        List<String> topTags = extractTopTags(meeting);
        InfoOutput infoOutput = buildInfo(meeting);
        LocationOutput locationOutput = buildLocation(meeting);
        TimeOutput timeOutput = buildTime(meeting);
        HostOutput hostOutput = buildHost(meeting);

        return MeetingResponse.builder()
                .id(meeting.getId())
                .thumbnailUrl(meeting.getThumbnailUrl())
                .topTags(topTags)
                .infoOutput(infoOutput)
                .locationOutput(locationOutput)
                .timeOutput(timeOutput)
                .hostOutput(hostOutput)
                .build();
    }

    private static List<String> extractTopTags(Meeting meeting) {
        return meeting.getHashtags().stream()
                .map(Hashtag::getName)
                .collect(Collectors.toList());
    }

    private static InfoOutput buildInfo(Meeting meeting) {
        return InfoOutput.create(
                meeting.getTitle(),
                meeting.getMaxParticipants(),
                meeting.getCurrentParticipants());
    }

    private static LocationOutput buildLocation(Meeting meeting) {
        MeetingAddress address = meeting.getMeetingAddress();
        return LocationOutput.create(address.getLocation(), address.getDetailLocation());
    }

    private static TimeOutput buildTime(Meeting meeting) {
        MeetingTime time = meeting.getMeetingTime();
        return TimeOutput.create(time.getStartTime(), time.getEndTime(), time.getDuration());
    }

    private static HostOutput buildHost(Meeting meeting) {
        User host = meeting.getHost();
        return HostOutput.builder()
                .name(host.getNickname())
                .profileImage(host.getProfileImage())
                .build();
    }
}
