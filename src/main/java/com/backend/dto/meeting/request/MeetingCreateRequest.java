package com.backend.dto.meeting.request;

import com.backend.dto.meeting.dto.HashtagDTO;
import com.backend.dto.meeting.dto.ImageDTO;
import com.backend.dto.meeting.dto.LocationDTO;
import com.backend.dto.meeting.dto.MeetingInfoDTO;
import com.backend.dto.meeting.dto.TimeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingCreateRequest {
    private final String category;

    @JsonProperty("info")
    private final MeetingInfoDTO meetingInfoDTO;

    @JsonUnwrapped
    private final HashtagDTO hashtagDTO;

    @JsonProperty("location")
    private final LocationDTO locationDTO;

    @JsonProperty("time")
    private final TimeDTO timeDTO;

    @JsonProperty("image")
    private final ImageDTO imageDTO;
}
