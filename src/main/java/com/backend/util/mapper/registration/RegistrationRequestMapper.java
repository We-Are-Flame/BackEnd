package com.backend.util.mapper.registration;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;

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
