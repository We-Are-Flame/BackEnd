package com.backend.dto.meeting.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyMeetingResponseList {
    private int count;
    @JsonProperty("content")
    private final List<MyMeetingResponse> myMeetingResponse;
}
