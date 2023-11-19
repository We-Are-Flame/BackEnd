package com.backend.dto.meeting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingInfoDTO {
    private final String name;
    private final Integer maxParticipants;
    private final String description;
    private final String category;
}
