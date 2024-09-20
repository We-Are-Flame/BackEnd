package com.backend.meeting.domain.meeting.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingTime {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;

    @Builder
    public MeetingTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime).toHours();
    }

    @Access(AccessType.FIELD)
    public boolean isBeforeNow() {
        return this.endTime.isBefore(LocalDateTime.now());
    }
}
