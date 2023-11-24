package com.backend.dto.comment.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentData {
    private String profileImage;
    private String nickname;
    private String description;
    private LocalDateTime createdAt;
}
