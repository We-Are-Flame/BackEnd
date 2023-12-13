package com.backend.dto.meeting.response.output;

import com.backend.dto.meeting.dto.TimeDTO;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TimeOutput extends TimeDTO {
    private final Long duration;

    @QueryProjection
    public TimeOutput(LocalDateTime startTime, LocalDateTime endTime, Long duration) {
        super(startTime, endTime);
        this.duration = duration;
    }
}
