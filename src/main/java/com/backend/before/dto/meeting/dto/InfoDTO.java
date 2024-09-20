package com.backend.before.dto.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class InfoDTO {
    @Schema(example = "술먹을 사람 들어왕", description = "모임명")
    private final String title;
    @Schema(example = "5", description = "최대 모집 인원")
    private final Integer maxParticipants;
}
