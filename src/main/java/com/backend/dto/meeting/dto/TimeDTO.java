package com.backend.dto.meeting.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeDTO {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
}

