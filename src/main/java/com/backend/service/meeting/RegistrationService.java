package com.backend.service.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.backend.repository.meeting.MeetingRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final MeetingRegistrationRepository meetingRegistrationRepository;

    @Transactional
    public void createOwnerStatus(Meeting meeting, User user) {
        MeetingRegistration registration = MeetingRegistration.builder()
                .meeting(meeting)
                .user(user)
                .role(RegistrationRole.OWNER)
                .status(RegistrationStatus.ACCEPTED)
                .build();

        meetingRegistrationRepository.save(registration);
    }
}

