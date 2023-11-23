package com.backend.dto.meeting.request.create.input;

import com.backend.dto.meeting.dto.InfoDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InfoInput extends InfoDTO {
    @Schema(example = "날도 추운데 한잔 적시고 자실 분", description = "모집 내용")
    private final String description;

    @JsonCreator
    public static InfoInput create(String title, String description, Integer maxParticipants) {
        return InfoInput.builder()
                .title(title)
                .description(description)
                .maxParticipants(maxParticipants).build();
    }
}
