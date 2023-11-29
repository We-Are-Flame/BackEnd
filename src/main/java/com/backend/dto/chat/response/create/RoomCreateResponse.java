package com.backend.dto.chat.response.create;

import com.backend.dto.bases.BaseResponse;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.bases.ResponseStatus;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RoomCreateResponse extends BaseResponse {
    private final Long id;
    public static RoomCreateResponse success(Long id) {
        return RoomCreateResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.CHAT_ROOM_CREATION_SUCCESS.getMessage())
                .build();
    }
}
