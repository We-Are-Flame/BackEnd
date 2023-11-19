package com.backend.repository.meeting;

import com.backend.entity.meeting.MeetingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingImageRepository extends JpaRepository<MeetingImage, Long> {
}
