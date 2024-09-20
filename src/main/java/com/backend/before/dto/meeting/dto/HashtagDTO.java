package com.backend.before.dto.meeting.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HashtagDTO {
    @ArraySchema(
            arraySchema = @Schema(description = "해시태그 목록, 예: ['#술', '#남녀상관X']", type = "array"),
            schema = @Schema(type = "string", example = "#술")
    )
    private final List<String> hashtags;
}
