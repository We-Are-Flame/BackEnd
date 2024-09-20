package com.backend.before.dto.meeting.response.output;

import com.backend.before.dto.meeting.dto.TimeDTO;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DetailTimeOutput extends TimeDTO {
    private final LocalDateTime createdAt;
    private final Long duration;

    @QueryProjection
    public DetailTimeOutput(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createdAt, Long duration) {
        super(startTime, endTime);
        this.createdAt = createdAt;
        this.duration = duration;
    }
}
