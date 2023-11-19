package com.backend.dto.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationDTO {
    @Schema(example = "경북 구미시", description = "주소명")
    private final String location;
    @Schema(example = "금오공대 앞 히로지마", description = "상세 주소")
    private final String detailLocation;
}
