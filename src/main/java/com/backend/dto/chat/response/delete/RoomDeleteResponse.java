package com.backend.dto.chat.response.delete;

import com.backend.dto.bases.BaseResponse;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.bases.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RoomDeleteResponse extends BaseResponse {
    private final Long id;

    public static RoomDeleteResponse success(Long id) {
        return RoomDeleteResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.CHAT_ROOM_DELETE_SUCCESS.getMessage())
                .build();
    }
}
