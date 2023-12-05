package com.backend.service.meeting;

import com.backend.dto.registration.response.RegistrationResponse;
import com.backend.dto.registration.response.RegistrationResponseList;
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
import com.backend.repository.meeting.meeting.MeetingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {
    private final MeetingRegistrationRepository meetingRegistrationRepository;
    private final MeetingRepository meetingRepository;

    public void createOwnerStatus(Meeting meeting, User user) {
        MeetingRegistration registration = buildRegistration(meeting, user, RegistrationRole.OWNER,
                RegistrationStatus.ACCEPTED);
        meetingRegistrationRepository.save(registration);
    }

    public Long applyMeeting(Long meetingId, User user) {
        Meeting meeting = findMeetingById(meetingId);
        checkRegistrationDuplication(meeting, user);
        MeetingRegistration registration = buildRegistration(meeting, user, RegistrationRole.MEMBER,
                RegistrationStatus.PENDING);
        return meetingRegistrationRepository.save(registration).getId();
    }

    public Long cancelMeeting(Long meetingId, User user) {
        MeetingRegistration registration = findRegistrationByMeetingAndUser(meetingId, user);
        try {
            registration.checkIfPending();
            meetingRegistrationRepository.delete(registration);
        } catch (IllegalStateException e) {
            handleIllegalStateException(e);
        }
        return registration.getId();
    }

    @Transactional(readOnly = true)
    public RegistrationResponseList getRegistrations(Long meetingId) {
        List<RegistrationResponse> responseList = fetchRegistrationResponses(findMeetingById(meetingId));
        return new RegistrationResponseList(responseList.size(), responseList);
    }

    public Long acceptApply(List<Long> registrationIds) {
        return processBulkUpdate(registrationIds, RegistrationStatus.ACCEPTED);
    }

    public Long rejectApply(List<Long> registrationIds) {
        return processBulkUpdate(registrationIds, RegistrationStatus.REJECTED);
    }

    private Meeting findMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private MeetingRegistration findRegistrationByMeetingAndUser(Long meetingId, User user) {
        return meetingRegistrationRepository.findByMeetingAndUser(findMeetingById(meetingId), user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));
    }

    private void checkRegistrationDuplication(Meeting meeting, User user) {
        if (meetingRegistrationRepository.existsByMeetingAndUser(meeting, user)) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_REGISTERED);
        }
    }

    private MeetingRegistration buildRegistration(Meeting meeting, User user, RegistrationRole role,
                                                  RegistrationStatus status) {
        return MeetingRegistration.builder()
                .meeting(meeting)
                .user(user)
                .role(role)
                .status(status)
                .build();
    }

    private Long processBulkUpdate(List<Long> registrationIds, RegistrationStatus status) {
        registrationIds.forEach(registrationId -> updateRegistrationStatus(registrationId, status));
        return registrationIds.get(registrationIds.size() - 1);
    }

    private List<RegistrationResponse> fetchRegistrationResponses(Meeting meeting) {
        return meetingRegistrationRepository.findByMeetingAndStatus(meeting, RegistrationStatus.PENDING)
                .stream()
                .filter(MeetingRegistration::isNotOwner)
                .map(this::convertToRegistrationResponse)
                .toList();
    }

    private RegistrationResponse convertToRegistrationResponse(MeetingRegistration registration) {
        User registeredUser = registration.getUser();
        return RegistrationResponse.builder()
                .id(registration.getId())
                .nickname(registeredUser.getNickname())
                .profileImage(registeredUser.getProfileImage())
                .temperature(registeredUser.getTemperature())
                .participateStatus(registration.getStatus())
                .build();
    }

    private void updateRegistrationStatus(Long registrationId, RegistrationStatus status) {
        try {
            MeetingRegistration registration = meetingRegistrationRepository.findById(registrationId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));
            registration.checkIfPending();
            countRegistrations(registration, status);
            registration.updateStatus(status);
            meetingRegistrationRepository.save(registration);
        } catch (IllegalStateException e) {
            handleIllegalStateException(e);
        }
    }

    private void countRegistrations(MeetingRegistration registration, RegistrationStatus status) {
        if (status.equals(RegistrationStatus.ACCEPTED)) {
            Meeting meeting = registration.getMeeting();
            meeting.addCurrentParticipants();
            meetingRepository.save(meeting);
        }
    }

    private void handleIllegalStateException(IllegalStateException e) {
        throw new BadRequestException(e.getMessage());
    }
}
