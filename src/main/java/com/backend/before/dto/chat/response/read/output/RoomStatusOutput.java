package com.backend.before.dto.chat.response.read.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomStatusOutput {
    private final Boolean isOwner;
    private final Boolean isExpire;
}
