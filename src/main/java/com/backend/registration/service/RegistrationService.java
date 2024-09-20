package com.backend.registration.service;

import com.backend.before.dto.registration.response.AcceptResponse;
import com.backend.before.dto.registration.response.RegistrationResponse;
import com.backend.before.dto.registration.response.RegistrationResponseList;
import com.backend.before.dto.registration.response.RejectResponse;
import com.backend.before.entity.chat.ChatRoom;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.registration.entity.MeetingRegistration;
import com.backend.registration.entity.RegistrationStatus;
import com.backend.before.entity.notification.NotificationType;
import com.backend.before.entity.user.User;
import com.backend.before.exception.AlreadyExistsException;
import com.backend.before.exception.BadRequestException;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.before.repository.chat.ChatRoomRepository;
import com.backend.registration.repository.MeetingRegistrationRepository;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import com.backend.before.service.chat.RoomService;
import com.backend.before.service.notification.NotificationService;
import com.backend.before.util.mapper.registration.RegistrationRequestMapper;
import com.backend.before.util.mapper.registration.RegistrationResponseMapper;
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
    private final ChatRoomRepository chatRoomRepository;
    private final NotificationService notificationService;
    private final RoomService roomService;

    public Long applyMeeting(Long meetingId, User user) {
        Meeting meeting = fetchMeeting(meetingId);
        checkDuplicate(meeting, user);

        MeetingRegistration registration = RegistrationRequestMapper.buildPending(meeting, user);
        sendApplyNotification(meeting);

        return meetingRegistrationRepository.save(registration).getId();
    }

    public Long cancelMeeting(Long meetingId, User user) {
        MeetingRegistration registration = fetchMeetingByUser(meetingId, user);
        checkIfPending(registration);
        meetingRegistrationRepository.delete(registration);
        return registration.getId();
    }

    @Transactional(readOnly = true)
    public RegistrationResponseList getRegistrations(Long meetingId) {
        List<RegistrationResponse> responseList = fetchRegistration(meetingId);
        return new RegistrationResponseList(responseList.size(), responseList);
    }

    public AcceptResponse acceptApply(Long meetingId, List<Long> registrationIds) {
        ChatRoom chatRoom = fetchChatRoom(meetingId);
        List<User> users = fetchUsersByRegistration(registrationIds);

        roomService.addUsersInChatRoom(users, meetingId, chatRoom.getUuid());
        updateRegistrationsStatus(registrationIds, RegistrationStatus.ACCEPTED);

        return RegistrationResponseMapper.buildAccept(chatRoom.getUuid(), registrationIds, users);
    }

    public RejectResponse rejectApply(List<Long> registrationIds) {
        updateRegistrationsStatus(registrationIds, RegistrationStatus.REJECTED);
        return RegistrationResponseMapper.buildReject(registrationIds);
    }

    private void updateRegistrationsStatus(List<Long> registrationIds, RegistrationStatus status) {
        List<MeetingRegistration> registrations = meetingRegistrationRepository.findByIdIn(registrationIds);
        registrations.forEach(registration -> {
            checkIfPending(registration);
            countRegistrations(registration, status);
            registration.updateStatus(status);

            sendAcceptOrReject(status, registration);
        });
        meetingRegistrationRepository.saveAll(registrations);
    }

    private void countRegistrations(MeetingRegistration registration, RegistrationStatus status) {
        if (status.equals(RegistrationStatus.ACCEPTED)) {
            Meeting meeting = registration.getMeeting();
            meeting.addCurrentParticipants();
            meetingRepository.save(meeting);
        }
    }

    private Meeting fetchMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private MeetingRegistration fetchMeetingByUser(Long meetingId, User user) {
        return meetingRegistrationRepository.findByMeetingIdAndUser(meetingId, user)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));
    }

    private List<RegistrationResponse> fetchRegistration(Long meetingId) {
        return meetingRegistrationRepository.findAllInPending(meetingId)
                .stream()
                .map(RegistrationResponseMapper::buildRegistrationResponse)
                .toList();
    }

    private ChatRoom fetchChatRoom(Long meetingId) {
        return chatRoomRepository.findByMeetingId(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private List<User> fetchUsersByRegistration(List<Long> registrationIds) {
        List<User> users = meetingRegistrationRepository.findUsersByRegistrationIds(registrationIds);

        checkUsersEmpty(users);

        return users;
    }

    private void checkDuplicate(Meeting meeting, User user) {
        if (meetingRegistrationRepository.existsByMeetingAndUser(meeting, user)) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_REGISTERED);
        }
    }

    private void checkIfPending(MeetingRegistration registration) {
        if (!registration.getStatus().equals(RegistrationStatus.PENDING)) {
            throw new BadRequestException(ErrorMessages.CAN_CHANGE_IN_PENDING);
        }
    }

    private void checkUsersEmpty(List<User> users) {
        if (users.isEmpty()) {
            throw new NotFoundException(ErrorMessages.NOT_EXIST_USERS);
        }
    }

    private void sendApplyNotification(Meeting meeting) {
        notificationService.sendNotification(meeting.getHost(), meeting,
                NotificationType.MEETING_REQUEST
        );
    }

    private void sendAcceptOrReject(RegistrationStatus status, MeetingRegistration registration) {
        NotificationType notificationType;
        if (status == RegistrationStatus.ACCEPTED) {
            notificationType = NotificationType.MEETING_ACCEPTED;
        } else if (status == RegistrationStatus.REJECTED) {
            notificationType = NotificationType.MEETING_REJECTED;
        } else {
            throw new IllegalArgumentException("Unexpected status: " + status);
        }

        notificationService.sendNotification(
                registration.getUser(), registration.getMeeting(),
                notificationType
        );
    }
}
