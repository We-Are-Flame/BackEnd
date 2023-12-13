package com.backend.repository.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRegistrationRepository extends JpaRepository<MeetingRegistration, Long> {
    boolean existsByMeetingAndUser(Meeting meeting, User user);

    Optional<MeetingRegistration> findByMeetingIdAndUser(Long meetingId, User user);

    List<MeetingRegistration> findByMeetingIdAndStatusAndRole(Long meetingId, RegistrationStatus status,
                                                              RegistrationRole role);

    List<MeetingRegistration> findByIdIn(List<Long> registrationIds);

    @Query("SELECT mr.user FROM MeetingRegistration mr WHERE mr.id IN :registrationIds")
    List<User> findUsersByRegistrationIds(List<Long> registrationIds);

    default List<MeetingRegistration> findAllInPending(Long meetingId) {
        return findByMeetingIdAndStatusAndRole(meetingId, RegistrationStatus.PENDING, RegistrationRole.MEMBER);
    }
}
