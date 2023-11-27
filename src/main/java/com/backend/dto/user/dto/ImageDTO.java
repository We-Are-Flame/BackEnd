package com.backend.dto.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ImageDTO {
    @Schema(example = "프로필.jpg", description = "프로필 이미지 링크")
    private final String profileImageUrl;

}