package com.backend.dto.meeting.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record NotEndResponseList(
        @JsonProperty("content")
        List<NotEndResponse> notEndResponses,
        int count) {
}
