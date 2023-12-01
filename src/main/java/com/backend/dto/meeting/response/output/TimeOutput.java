package com.backend.dto.meeting.response.output;

import com.backend.dto.meeting.dto.TimeDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TimeOutput extends TimeDTO {
    private final Long duration;
}
