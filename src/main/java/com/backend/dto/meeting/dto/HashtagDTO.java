package com.backend.dto.meeting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagDTO {
    @ArraySchema(
            arraySchema = @Schema(description = "해시태그 목록, 예: ['술', '남녀상관X']", type = "array"),
            schema = @Schema(type = "string", example = "술")
    )
    private final List<String> hashtags;

    @JsonCreator
    public HashtagDTO(@JsonProperty("hashtags") List<String> hashtags) {
        this.hashtags = hashtags;
    }
}
