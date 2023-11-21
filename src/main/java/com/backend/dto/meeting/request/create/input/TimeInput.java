package com.backend.dto.meeting.request.create.input;

import com.backend.dto.meeting.dto.TimeDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TimeInput extends TimeDTO {
    @JsonCreator
    public static TimeInput create(LocalDateTime startTime, LocalDateTime endTime) {
        return TimeInput.builder().startTime(startTime).endTime(endTime).build();
    }
}
