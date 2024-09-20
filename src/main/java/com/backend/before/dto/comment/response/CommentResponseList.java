package com.backend.before.dto.comment.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseList {
    private int count;
    private List<CommentResponse> commentResponses;
}
