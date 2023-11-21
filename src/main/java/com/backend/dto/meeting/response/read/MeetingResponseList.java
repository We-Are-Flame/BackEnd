package com.backend.dto.meeting.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingResponseList {
    @JsonProperty("content")
    List<MeetingResponse> responses;
}
