package com.backend.dto.meeting.response.read.output;

import com.backend.dto.meeting.dto.InfoDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DetailInfoOutput extends InfoDTO {
    private final Integer currentParticipants;
    private final String description;
}

