package com.backend.dto.comment.response;

import com.backend.dto.bases.BaseResponse;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.bases.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CommentCreateResponse extends BaseResponse {
    private final Long id;

    public static CommentCreateResponse success(Long id) {
        return CommentCreateResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.COMMENT_CREATION_SUCCESS.getMessage())
                .build();
    }
}
