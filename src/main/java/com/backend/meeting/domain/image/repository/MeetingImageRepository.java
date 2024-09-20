package com.backend.meeting.domain.image.repository;

import com.backend.meeting.domain.image.entity.MeetingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingImageRepository extends JpaRepository<MeetingImage, Long> {
}
