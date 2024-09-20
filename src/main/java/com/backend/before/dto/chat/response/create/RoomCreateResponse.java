package com.backend.before.dto.chat.response.create;

import com.backend.before.dto.common.BaseResponse;
import com.backend.before.dto.common.ResponseMessage;
import com.backend.before.dto.common.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RoomCreateResponse extends BaseResponse {
    private final String id;

    public static RoomCreateResponse success(String id) {
        return RoomCreateResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.CHAT_ROOM_CREATION_SUCCESS.getMessage())
                .build();
    }
}
