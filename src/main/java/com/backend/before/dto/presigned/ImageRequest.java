package com.backend.before.dto.presigned;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ImageRequest(
        @Schema(example = "test", description = "파일명")
        @NotBlank(message = "파일명을 입력하셔야 됩니다!")
        String fileName,

        @Schema(example = "image/jpeg", description = "이미지 타입")
        @NotBlank(message = "이미지 타입을 입력하셔야 됩니다!")
        String fileType
) {
}
