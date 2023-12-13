package com.backend.dto.chat.response.read;

import com.backend.entity.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatUserResponse {

    private Long id;
    private String nickname;
    @JsonProperty("profile_image")
    private String profileImage;

    public static ChatUserResponse from(User user) {
        return ChatUserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
