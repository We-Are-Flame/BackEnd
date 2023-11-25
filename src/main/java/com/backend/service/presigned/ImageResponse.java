package com.backend.service.presigned;

import java.net.URL;

public record ImageResponse(URL presignedUrl, String imageUrl) {
}
