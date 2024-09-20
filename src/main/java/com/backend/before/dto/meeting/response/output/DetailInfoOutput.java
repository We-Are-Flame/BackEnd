package com.backend.before.dto.meeting.response.output;

import com.backend.before.dto.meeting.dto.InfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@SuperBuilder
@Slf4j
public class DetailInfoOutput extends InfoDTO {
    private final Integer currentParticipants;
    private final String description;

    @QueryProjection
    public DetailInfoOutput(String title, String description, Integer maxParticipants, Integer currentParticipants) {
        super(title, maxParticipants);
        this.currentParticipants = currentParticipants;
        this.description = description;
    }

    @JsonIgnore
    public boolean isFull() {
        return currentParticipants.equals(getMaxParticipants());
    }
}

