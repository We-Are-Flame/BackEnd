package com.backend.before.dto.meeting.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyMeetingResponseList {
    @JsonProperty("content")
    private final List<MyMeetingResponse> myMeetingResponse;
    private int count;
}
