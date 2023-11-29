package com.backend.dto.chat.request.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomCreateRequest{

    @JsonProperty("meeting_id")
    private Long meetingId;
    @JsonProperty("room_name")
    private String roomName;

}
