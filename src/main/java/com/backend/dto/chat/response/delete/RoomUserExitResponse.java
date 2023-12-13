package com.backend.dto.chat.response.delete;

import com.backend.dto.common.BaseResponse;
import com.backend.dto.common.ResponseMessage;
import com.backend.dto.common.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RoomUserExitResponse extends BaseResponse {
    private final Long id;

    public static RoomUserExitResponse success(Long id) {
        return RoomUserExitResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.CHAT_ROOM_USER_EXIT_SUCCESS.getMessage())
                .build();
    }
}
