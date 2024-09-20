package com.backend.service.meeting;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.registration.entity.MeetingRegistration;
import com.backend.registration.entity.RegistrationRole;
import com.backend.registration.entity.RegistrationStatus;
import com.backend.before.entity.user.User;
import com.backend.before.exception.AlreadyExistsException;
import com.backend.registration.repository.MeetingRegistrationRepository;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import java.util.Optional;

import com.backend.registration.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private MeetingRegistrationRepository meetingRegistrationRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private static Long[] userIdsProvider() {
        return new Long[]{1L, 2L, 3L}; // 다양한 사용자 ID를 제공합니다.
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .nickname("TestUser" + id)
                .profileImage("https://example.com/profile" + id + ".jpg")
                .temperature(36)
                .email("test" + id + "@example.com")
                .build();
    }

    private Meeting createMeeting(User host) {
        return Meeting.builder()
                .title("Test Meeting")
                .description("This is a test meeting")
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .currentParticipants(0)
                .maxParticipants(10)
                .host(host)
                .build();
    }

    @ParameterizedTest
    @DisplayName("모임 신청 테스트")
    @MethodSource("userIdsProvider")
    public void testApplyMeeting(Long userId) {
        User host = createUser(999L); // 호스트용 사용자 생성
        User applicant = createUser(userId); // 신청자용 사용자 생성
        Meeting meeting = createMeeting(host);

        MeetingRegistration registration = MeetingRegistration.builder()
                .id(userId) // ID 설정
                .role(RegistrationRole.MEMBER)
                .status(RegistrationStatus.PENDING)
                .user(applicant)
                .meeting(meeting)
                .build();

        when(meetingRepository.findById(any())).thenReturn(Optional.of(meeting));
        when(meetingRegistrationRepository.save(any(MeetingRegistration.class))).thenReturn(registration);
        when(meetingRegistrationRepository.existsByMeetingAndUser(meeting, applicant)).thenReturn(false);

        Long registrationId = registrationService.applyMeeting(meeting.getId(), applicant);

        assertEquals(registration.getId(), registrationId);
        verify(meetingRegistrationRepository).save(any(MeetingRegistration.class));
        assertEquals(RegistrationRole.MEMBER, registration.getRole());
        assertEquals(RegistrationStatus.PENDING, registration.getStatus());
    }

    @ParameterizedTest
    @DisplayName("중복 모임 신청 검사 테스트")
    @MethodSource("userIdsProvider")
    public void testCheckForDuplicateRegistration(Long userId) {
        User host = createUser(999L); // 호스트용 사용자 생성
        User applicant = createUser(userId); // 신청자용 사용자 생성
        Meeting meeting = createMeeting(host);

        MeetingRegistration registration = MeetingRegistration.builder()
                .id(userId) // ID 설정
                .role(RegistrationRole.MEMBER)
                .status(RegistrationStatus.PENDING)
                .user(applicant)
                .meeting(meeting)
                .build();

        when(meetingRepository.findById(any())).thenReturn(Optional.of(meeting));
        when(meetingRegistrationRepository.save(any(MeetingRegistration.class))).thenReturn(registration);

        // 중복이 없는 경우 테스트
        when(meetingRegistrationRepository.existsByMeetingAndUser(meeting, applicant)).thenReturn(false);
        assertDoesNotThrow(() -> registrationService.applyMeeting(meeting.getId(), applicant));

        // 중복이 있는 경우 테스트
        when(meetingRegistrationRepository.existsByMeetingAndUser(meeting, applicant)).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> registrationService.applyMeeting(meeting.getId(), applicant));
    }
}
