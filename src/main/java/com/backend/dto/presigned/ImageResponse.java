package com.backend.dto.presigned;

import java.net.URL;

public record ImageResponse(URL presignedUrl, String imageUrl) {

}
