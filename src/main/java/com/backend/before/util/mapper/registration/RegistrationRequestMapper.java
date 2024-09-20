package com.backend.before.util.mapper.registration;

import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.registration.entity.MeetingRegistration;
import com.backend.registration.entity.RegistrationRole;
import com.backend.registration.entity.RegistrationStatus;
import com.backend.before.entity.user.User;

public class RegistrationRequestMapper {
    public static MeetingRegistration buildOwner(Meeting meeting, User user) {
        return MeetingRegistration.builder()
                .meeting(meeting)
                .user(user)
                .role(RegistrationRole.OWNER)
                .status(RegistrationStatus.ACCEPTED)
                .build();
    }

    public static MeetingRegistration buildPending(Meeting meeting, User user) {
        return MeetingRegistration.builder()
                .meeting(meeting)
                .user(user)
                .role(RegistrationRole.MEMBER)
                .status(RegistrationStatus.PENDING)
                .build();
    }
}
