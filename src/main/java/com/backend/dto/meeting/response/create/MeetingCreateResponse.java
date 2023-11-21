package com.backend.dto.meeting.response.create;

import com.backend.dto.bases.BaseResponse;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.bases.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MeetingCreateResponse extends BaseResponse {
    private final Long id;

    public static MeetingCreateResponse success(Long id) {
        return MeetingCreateResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.MEETING_CREATION_SUCCESS.getMessage())
                .build();
    }
}
