package com.backend.before.dto.meeting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class TimeDTO {
    @Schema(example = "2023-11-19T20:00:00.000Z", description = "시작 시간")
    private final LocalDateTime startTime;
    @Schema(example = "2023-11-20T00:00:00.000Z", description = "시작 시간")
    private final LocalDateTime endTime;

    @JsonIgnore
    public boolean isExpired() {
        return endTime.isBefore(LocalDateTime.now());
    }
}
