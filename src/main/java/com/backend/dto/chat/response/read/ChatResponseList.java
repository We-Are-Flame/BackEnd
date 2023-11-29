package com.backend.dto.chat.response.read;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatResponseList {
    @JsonProperty("content")
    private final List<ChatResponse> myMeetingResponse;
    private int count;
}
