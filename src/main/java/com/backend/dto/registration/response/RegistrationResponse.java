package com.backend.dto.registration.response;

import com.backend.entity.meeting.RegistrationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationResponse {
    private final Long id;
    private final String nickname;
    private final String profileImage;
    private final Integer temperature;
    private final RegistrationStatus participateStatus;
}
