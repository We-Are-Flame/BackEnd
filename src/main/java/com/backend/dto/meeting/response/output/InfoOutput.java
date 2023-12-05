package com.backend.dto.meeting.response.output;

import com.backend.dto.meeting.dto.InfoDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InfoOutput extends InfoDTO {
    private final Integer currentParticipants;

    @QueryProjection
    public InfoOutput(String title, Integer maxParticipants, Integer currentParticipants) {
        super(title, maxParticipants);
        this.currentParticipants = currentParticipants;
    }
}
