package com.backend.dto.chat.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class ChatUserResponseList {
    @JsonProperty("content")
    private final List<ChatUserResponse> roomUserResponses;
    private int count;
}
