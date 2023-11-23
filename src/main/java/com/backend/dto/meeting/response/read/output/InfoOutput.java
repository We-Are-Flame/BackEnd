package com.backend.dto.meeting.response.read.output;

import com.backend.dto.meeting.dto.InfoDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InfoOutput extends InfoDTO {
    private final Integer currentParticipants;

    public static InfoOutput create(String title, Integer maxParticipants, Integer currentParticipants) {
        return InfoOutput.builder()
                .title(title)
                .maxParticipants(maxParticipants)
                .currentParticipants(currentParticipants)
                .build();
    }
}
