package com.backend.dto.meeting.request;

import com.backend.dto.meeting.common.ImageDTO;
import com.backend.dto.meeting.common.LocationDTO;
import com.backend.dto.meeting.common.TimeDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingCreateRequest {
    private final String name;
    private final Integer maxParticipants;
    private final String description;
    private final String category;
    private final List<String> hashtags;

    private final LocationDTO location;
    private final TimeDTO time;
    private final ImageDTO image;

    public boolean hasThumbnail() {
        return image != null && image.getThumbnailUrl() != null;
    }
}
