package com.backend.dto.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingInfoDTO {
    @Schema(example = "술먹을 사람 들어왕",description = "모임명")
    private final String name;
    @Schema(example = "5",description = "최대 모집 인원")
    private final Integer maxParticipants;
    @Schema(example = "날도 추운데 한잔 적시고 자실 분",description = "모집 내용")
    private final String description;
}
