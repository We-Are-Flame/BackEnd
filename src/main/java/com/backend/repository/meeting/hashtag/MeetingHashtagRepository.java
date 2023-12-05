package com.backend.repository.meeting.hashtag;

import com.backend.entity.meeting.MeetingHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingHashtagRepository extends JpaRepository<MeetingHashtag, Long> {
}
