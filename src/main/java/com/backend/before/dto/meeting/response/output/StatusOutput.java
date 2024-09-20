package com.backend.before.dto.meeting.response.output;

import com.backend.registration.entity.RegistrationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatusOutput {
    private final Boolean isOwner;
    private final RegistrationStatus participateStatus;
    private final Boolean isExpire;
    private final Boolean isFull;
}
