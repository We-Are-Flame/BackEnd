package com.backend.before.dto.presigned;

import java.net.URL;

public record ImageResponse(URL presignedUrl, String imageUrl) {
}
