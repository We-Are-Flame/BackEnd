package com.backend.before.dto.chat.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatUserResponseList {
    @JsonProperty("content")
    private final List<ChatUserResponse> roomUserResponses;
    private int count;
}
