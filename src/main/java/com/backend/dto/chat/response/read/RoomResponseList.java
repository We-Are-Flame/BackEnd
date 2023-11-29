package com.backend.dto.chat.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoomResponseList {
    @JsonProperty("content")
    private final List<RoomResponse> roomResponses;
    private int count;
}
