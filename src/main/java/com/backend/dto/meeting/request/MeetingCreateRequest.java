package com.backend.dto.meeting.request;

import com.backend.dto.meeting.dto.HashtagDTO;
import com.backend.dto.meeting.dto.ImageDTO;
import com.backend.dto.meeting.dto.LocationDTO;
import com.backend.dto.meeting.dto.MeetingInfoDTO;
import com.backend.dto.meeting.dto.TimeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingCreateRequest {
    @Schema(example = "술",description = "카테고리")
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
