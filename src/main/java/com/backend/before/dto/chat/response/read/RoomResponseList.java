package com.backend.before.dto.chat.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponseList {
    @JsonProperty("content")
    private final List<RoomResponse> roomResponses;
    private int count;
}
