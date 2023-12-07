package com.backend.service.meeting;

import com.backend.dto.registration.response.AcceptResponse;
import com.backend.dto.registration.response.RegistrationResponse;
import com.backend.dto.registration.response.RegistrationResponseList;
import com.backend.dto.registration.response.RejectResponse;
import com.backend.entity.chat.ChatRoom;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.BadRequestException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.chat.ChatRoomRepository;
import com.backend.repository.meeting.MeetingRegistrationRepository;
import com.backend.repository.meeting.meeting.MeetingRepository;
import com.backend.service.chat.RoomService;
import com.backend.util.mapper.registration.RegistrationRequestMapper;
import com.backend.util.mapper.registration.RegistrationResponseMapper;
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
    private final RoomService roomService;

    public void createOwnerStatus(Meeting meeting, User user) {
        MeetingRegistration registration = RegistrationRequestMapper.buildOwner(meeting, user);
        meetingRegistrationRepository.save(registration);
    }

    public Long applyMeeting(Long meetingId, User user) {
        Meeting meeting = fetchMeeting(meetingId);
        checkDuplicate(meeting, user);
        MeetingRegistration registration = RegistrationRequestMapper.buildPending(meeting, user);
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
        addUserToChatRoom(chatRoom, registrationIds);
        updateRegistrationsStatus(registrationIds, RegistrationStatus.ACCEPTED);
        return RegistrationResponseMapper.buildAccept(chatRoom.getUuid(), registrationIds);
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
        });
        meetingRegistrationRepository.saveAll(registrations);
    }

    private void addUserToChatRoom(ChatRoom chatRoom, List<Long> registrationIds) {
        List<User> users = fetchUsersByRegistration(registrationIds);
        roomService.addUsersInChatRoom(users, chatRoom.getUuid());
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
}
