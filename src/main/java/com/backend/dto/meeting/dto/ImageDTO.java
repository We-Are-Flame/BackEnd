package com.backend.dto.meeting.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDTO {
    private final String thumbnailUrl;
    private final List<String> imageUrls;

    public boolean NotExistThumbnail() {
        return thumbnailUrl == null;
    }
}