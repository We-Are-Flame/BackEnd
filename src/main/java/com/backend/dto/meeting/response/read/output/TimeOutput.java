package com.backend.dto.meeting.response.read.output;

import com.backend.dto.meeting.dto.TimeDTO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TimeOutput extends TimeDTO {
    private final Long duration;

    public static TimeOutput create(LocalDateTime startTime, LocalDateTime endTime, Long duration) {
        return TimeOutput.builder().startTime(startTime).endTime(endTime).duration(duration).build();
    }
}
