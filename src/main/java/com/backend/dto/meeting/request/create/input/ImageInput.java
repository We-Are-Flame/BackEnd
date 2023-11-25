package com.backend.dto.meeting.request.create.input;

import com.backend.dto.meeting.dto.ImageDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ImageInput extends ImageDTO {
    @JsonCreator
    public static ImageInput create(String thumbnailUrl, List<String> imageUrls) {
        return ImageInput.builder()
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .build();
    }
}
