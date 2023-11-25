package com.backend.repository.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingRepositoryCustom {
    Page<Meeting> findAllWithDetails(Pageable pageable);

    Optional<Meeting> findMeetingWithDetailsById(Long id);

    List<Meeting> findAllByHost(User host);
}

