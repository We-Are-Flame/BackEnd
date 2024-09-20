package com.backend.before.dto.comment.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    @NotEmpty(message = "내용이 있어야 합니다!")
    private String description;
}
