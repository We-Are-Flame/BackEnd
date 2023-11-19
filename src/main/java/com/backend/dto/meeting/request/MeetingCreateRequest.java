package com.backend.dto.meeting.request;

import com.backend.dto.meeting.dto.ImageDTO;
import com.backend.dto.meeting.dto.LocationDTO;
import com.backend.dto.meeting.dto.MeetingInfoDTO;
import com.backend.dto.meeting.dto.TimeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingCreateRequest {
    @JsonProperty("info")
    private final MeetingInfoDTO meetingInfoDTO;

//    @JsonProperty("hashtag")
//    private final HashtagDTO hashtagDTO;

    @JsonProperty("location")
    private final LocationDTO locationDTO;

    @JsonProperty("time")
    private final TimeDTO timeDTO;

    @JsonProperty("image")
    private final ImageDTO imageDTO;
}
