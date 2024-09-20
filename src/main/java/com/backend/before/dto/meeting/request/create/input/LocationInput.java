package com.backend.before.dto.meeting.request.create.input;

import com.backend.before.dto.meeting.dto.LocationDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LocationInput extends LocationDTO {
    @JsonCreator
    public static LocationInput create(String location, String detailLocation, String latitude, String longtitude) {
        return LocationInput.builder()
                .location(location)
                .detailLocation(detailLocation)
                .latitude(latitude)
                .longitude(longtitude)
                .build();
    }
}
