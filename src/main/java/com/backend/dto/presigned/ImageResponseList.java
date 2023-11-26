package com.backend.dto.presigned;

import com.backend.service.presigned.ImageResponse;
import java.util.List;

public record ImageResponseList(List<ImageResponse> imageList) {
}
