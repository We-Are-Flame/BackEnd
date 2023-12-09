package com.backend.dto.meeting.response.output;

import com.backend.dto.meeting.dto.InfoDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DetailInfoOutput extends InfoDTO {
    private final Integer currentParticipants;
    private final String description;

    @QueryProjection
    public DetailInfoOutput(String title, String description, Integer maxParticipants, Integer currentParticipants) {
        super(title, maxParticipants);
        this.currentParticipants = currentParticipants;
        this.description = description;
    }

    public boolean isFull() {
        return currentParticipants.equals(getMaxParticipants());
    }
}

