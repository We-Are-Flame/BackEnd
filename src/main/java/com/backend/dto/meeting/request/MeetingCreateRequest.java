package com.backend.dto.meeting.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingCreateRequest {
    private final String name;
    private final Integer maxParticipants;
    private final String description;
    private final String location;
    private final String detailLocation;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Integer duration;
    private final String category;
    private final List<String> hashtags;
    private final String thumbnailUrl;
    private final List<String> imageUrls;
}
