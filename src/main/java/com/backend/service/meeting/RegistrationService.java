package com.backend.service.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.MeetingRegistrationRepository;
import com.backend.repository.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final MeetingRegistrationRepository meetingRegistrationRepository;
    private final MeetingRepository meetingRepository;

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

    @Transactional
    public Long applyMeeting(Long meetingId, User user) {
        Meeting meeting = getMeeting(meetingId);
        checkForDuplicateRegistration(meeting, user);
        return createRegistration(meeting, user).getId();
    }

    private Meeting getMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private void checkForDuplicateRegistration(Meeting meeting, User user) {
        if (meetingRegistrationRepository.existsByMeetingAndUser(meeting, user)) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_REGISTRATER);
        }
    }

    private MeetingRegistration createRegistration(Meeting meeting, User user) {
        MeetingRegistration registration = MeetingRegistration.builder()
                .meeting(meeting)
                .user(user)
                .role(RegistrationRole.MEMBER)
                .status(RegistrationStatus.PENDING)
                .build();

        // 등록을 저장하고 ID를 반환
        return meetingRegistrationRepository.save(registration);
    }

    @Transactional
    public Long cancelMeeting(Long meetingId, User user) {
        Meeting meeting = getMeeting(meetingId);
        checkIfUserIsHost(meeting, user);
        MeetingRegistration registration = findRegistration(meeting, user);
        deleteRegistration(registration);
        return registration.getId();
    }

    private void checkIfUserIsHost(Meeting meeting, User user) {
        if (meeting.isUserOwner(user)) {
            throw new BadRequestException(ErrorMessages.CANNOT_CANCEL_OWNER_REGISTRATION);
        }
    }

    private MeetingRegistration findRegistration(Meeting meeting, User user) {
        return meetingRegistrationRepository.findByMeetingAndUser(meeting, user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));
    }

    private void deleteRegistration(MeetingRegistration registration) {
        meetingRegistrationRepository.delete(registration);
    }
}
