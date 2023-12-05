package com.backend.dto.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class LocationDTO {
    @Schema(example = "경북 구미시", description = "주소명")
    private final String location;
    @Schema(example = "금오공대 앞 히로시마", description = "상세 주소")
    private final String detailLocation;
    @Schema(example = "36.4250364688847", description = "위도")
    private final String latitude;
    @Schema(example = "128.167240393244", description = "경도")
    private final String longitude;
}
