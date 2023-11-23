package com.backend.dto.meeting.response.read;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingStatus {
    private final Boolean isOwner;
    private final Boolean isParticipate;
    private final Boolean isExpire;
}
