package com.backend.dto.meeting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationDTO {
    private final String location;
    private final String detailLocation;
}
