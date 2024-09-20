package com.backend.registration.implemantation;

import com.backend.before.entity.user.User;
import com.backend.before.util.mapper.registration.RegistrationRequestMapper;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.registration.entity.MeetingRegistration;
import com.backend.registration.repository.MeetingRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class RegistrationCreator {
    private final MeetingRegistrationRepository meetingRegistrationRepository;

    public void createOwnerStatus(Meeting meeting, User user) {
        MeetingRegistration registration = RegistrationRequestMapper.buildOwner(meeting, user);
        meetingRegistrationRepository.save(registration);
    }
}
