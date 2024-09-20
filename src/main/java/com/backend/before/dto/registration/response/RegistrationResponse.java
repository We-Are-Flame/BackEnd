package com.backend.before.dto.registration.response;

import com.backend.registration.entity.RegistrationStatus;
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
    private final Boolean isSchoolEmail;
}
