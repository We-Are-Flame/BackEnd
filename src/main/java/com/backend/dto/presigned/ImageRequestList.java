package com.backend.dto.presigned;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ImageRequestList(@Valid @NotNull(message = "이미지 정보를 입력하셔야 됩니다") List<ImageRequest> imageList) {
}
