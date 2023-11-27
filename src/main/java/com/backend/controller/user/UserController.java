package com.backend.controller.user;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.user.request.update.UserUpdateRequest;
import com.backend.dto.user.response.update.UserUpdateResponse;
import com.backend.entity.user.User;
import com.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PutMapping("nickname")
    @CheckUserNotNull
    ResponseEntity<UserUpdateResponse.Nickname> updateNickName(@RequestBody UserUpdateRequest.Nickname request,
                                                               @CurrentMember User user){
        Long id = userService.updateNickname(request, user.getId());
        UserUpdateResponse.Nickname response = UserUpdateResponse.Nickname.success(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("profile-image")
    @CheckUserNotNull
    ResponseEntity<UserUpdateResponse.ProfileImage> updateProfileImage(@RequestBody UserUpdateRequest.ProfileImage request,
                                                                       @CurrentMember User user){
        Long id = userService.updateProfileImage(request, user.getId());
        UserUpdateResponse.ProfileImage response = UserUpdateResponse.ProfileImage.success(id);
        return ResponseEntity.ok(response);
    }

}
