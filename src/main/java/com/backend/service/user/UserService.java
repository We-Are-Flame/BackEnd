package com.backend.service.user;

import com.backend.dto.user.request.update.UserUpdateRequest;
import com.backend.dto.user.response.read.UserResponse;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserResponse.MyPage getMyPage(User user) {
        return UserResponse.MyPage.from(user);
    }

    @Transactional
    public Long updateNickname(UserUpdateRequest.Nickname request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
        user.updateNickname(request.getNickname());
        return user.getId();
    }

    @Transactional
    public Long updateProfileImage(UserUpdateRequest.ProfileImage request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
        user.updateProfileImage(request.getImageInput().getProfileImageUrl());
        return user.getId();
    }

    @Transactional
    public Long updateUserNotification(UserUpdateRequest.Notification request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
        user.updateNotification(request.getIsNotification());
        return user.getId();
    }

    public UserResponse.Notification getUserNotification(User user) {
        return UserResponse.Notification.builder()
                .isUserNotification(user.getSetting().getIsUserNotification())
                .build();
    }

    public User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
    }
}
