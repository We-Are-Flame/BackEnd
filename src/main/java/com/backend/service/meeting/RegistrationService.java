package com.backend.service.meeting;

import com.backend.dto.registration.response.RegistrationResponse;
import com.backend.dto.registration.response.RegistrationResponseList;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.MeetingRegistrationRepository;
import com.backend.repository.meeting.MeetingRepository;
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
        Meeting meeting = getMeeting(meetingId);
        checkRegistrationDuplication(meeting, user);
        MeetingRegistration registration = buildRegistration(meeting, user, RegistrationRole.MEMBER,
                RegistrationStatus.PENDING);
        return saveRegistration(registration).getId();
    }

    public Long cancelMeeting(Long meetingId, User user) {
        Meeting meeting = getMeeting(meetingId);
        MeetingRegistration registration = findRegistration(meeting, user);
        deleteRegistration(registration);
        return registration.getId();
    }

    @Transactional(readOnly = true)
    public RegistrationResponseList getRegistration(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<RegistrationResponse> responseList = fetchRegistrationResponses(meeting);
        return new RegistrationResponseList(responseList.size(), responseList);
    }

    public Long acceptApply(Long registrationId) {
        return updateRegistrationStatus(registrationId, RegistrationStatus.ACCEPTED);
    }

    public Long rejectApply(Long registrationId) {
        return updateRegistrationStatus(registrationId, RegistrationStatus.REJECTED);
    }

    public Long acceptBulkApply(List<Long> registrationIds) {
        return processBulkUpdate(registrationIds, RegistrationStatus.ACCEPTED);
    }

    public Long rejectBulkApply(List<Long> registrationIds) {
        return processBulkUpdate(registrationIds, RegistrationStatus.REJECTED);
    }

    private Long updateRegistrationStatus(Long registrationId, RegistrationStatus status) {
        MeetingRegistration registration = meetingRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));

        registration.updateStatus(status);
        meetingRegistrationRepository.save(registration);

        return registration.getId();
    }

    private Long processBulkUpdate(List<Long> registrationIds, RegistrationStatus status) {
        registrationIds.forEach(registrationId -> updateRegistrationStatus(registrationId, status));
        return registrationIds.isEmpty() ? null : registrationIds.get(registrationIds.size() - 1);
    }

    private Meeting getMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private void checkRegistrationDuplication(Meeting meeting, User user) {
        if (meetingRegistrationRepository.existsByMeetingAndUser(meeting, user)) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_REGISTRATER);
        }
    }

    private MeetingRegistration findRegistration(Meeting meeting, User user) {
        return meetingRegistrationRepository.findByMeetingAndUser(meeting, user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));
    }

    private void deleteRegistration(MeetingRegistration registration) {
        meetingRegistrationRepository.delete(registration);
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

    private MeetingRegistration saveRegistration(MeetingRegistration registration) {
        return meetingRegistrationRepository.save(registration);
    }

    private List<RegistrationResponse> fetchRegistrationResponses(Meeting meeting) {
        return meetingRegistrationRepository.findByMeeting(meeting).stream()
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
}

