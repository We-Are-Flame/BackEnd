package com.backend.dto.chat.request.create;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomCreateRequest{
    private Long meetingId;
    private String roomName;

}
