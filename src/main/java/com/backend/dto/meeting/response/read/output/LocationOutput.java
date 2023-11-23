package com.backend.dto.meeting.response.read.output;

import com.backend.dto.meeting.dto.LocationDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LocationOutput extends LocationDTO {
    public static LocationOutput create(String location, String detailLocation) {
        return LocationOutput.builder().location(location).detailLocation(detailLocation).build();
    }
}
