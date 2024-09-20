package com.backend.meeting.domain.hashtag.repository;

import com.backend.meeting.domain.hashtag.entity.MeetingHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingHashtagRepository extends JpaRepository<MeetingHashtag, Long> {
}
