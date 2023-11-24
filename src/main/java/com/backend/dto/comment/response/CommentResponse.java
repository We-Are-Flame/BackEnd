package com.backend.dto.comment.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private int count;
    private List<CommentData> content;
}
