package com.backend.before.dto.user.request.update.input;

import com.backend.before.dto.user.dto.ImageDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ImageInput extends ImageDTO {
    @JsonCreator
    public static ImageInput update(String profileImageUrl) {
        return ImageInput.builder()
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
