package com.backend.dto.meeting.response.read.output;

import com.backend.dto.meeting.dto.TimeDTO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DetailTimeOutput extends TimeDTO {
    private final LocalDateTime createdAt;
    private final Long duration;
}
