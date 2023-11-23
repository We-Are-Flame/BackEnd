package com.backend.dto.meeting.request.create.input;

import com.backend.dto.meeting.dto.LocationDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LocationInput extends LocationDTO {
    @JsonCreator
    public static LocationInput create(String location, String detailLocation) {
        return LocationInput.builder()
                .location(location)
                .detailLocation(detailLocation)
                .build();
    }
}
