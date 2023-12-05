package com.backend.dto.meeting.response.output;

import com.backend.dto.meeting.dto.LocationDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LocationOutput extends LocationDTO {
    @QueryProjection
    public LocationOutput(String location, String detailLocation, String latitude, String longitude) {
        super(location, detailLocation, latitude, longitude);
    }
}
