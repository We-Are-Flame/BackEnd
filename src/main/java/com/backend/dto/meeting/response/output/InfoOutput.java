package com.backend.dto.meeting.response.output;

import com.backend.dto.meeting.dto.InfoDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InfoOutput extends InfoDTO {
    private final Integer currentParticipants;
}
