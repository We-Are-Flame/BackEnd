package com.backend.dto.user.request.update;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


public class UserUpdateRequest {

    @Getter
    @Setter
    public static class Nickname{
        @NotEmpty(message = "내용이 있어야 합니다!")
        private String nickname;
    }


}
