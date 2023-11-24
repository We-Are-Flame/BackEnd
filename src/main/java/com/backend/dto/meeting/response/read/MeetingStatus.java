package com.backend.dto.meeting.response.read;

import com.backend.entity.meeting.RegistrationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingStatus {
    private final Boolean isOwner;
    private final RegistrationStatus participateStatus;
    private final Boolean isExpire;
}
