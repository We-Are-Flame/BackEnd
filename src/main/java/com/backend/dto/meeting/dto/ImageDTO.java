package com.backend.dto.meeting.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDTO {
    @Schema(example = "썸네일.jpg", description = "썸네일 링크")
    private final String thumbnailUrl;
    @ArraySchema(
            arraySchema = @Schema(description = "이미지 목록, 예: ['image1.jpg', 'image2.jpg']", type = "array"),
            schema = @Schema(type = "string", example = "image.jpg")
    )
    private final List<String> imageUrls;

    public boolean NotExistThumbnail() {
        return thumbnailUrl == null;
    }
}
