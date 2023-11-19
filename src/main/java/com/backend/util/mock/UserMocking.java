package com.backend.util.mock;

import com.backend.entity.user.Setting;
import com.backend.entity.user.User;
import com.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMocking {
    private final UserRepository userRepository;

    public User findOrMockUser() {
        return userRepository.findById(1L).orElseGet(this::mockUser);
    }

    private User mockUser() {
        Setting mockSetting = Setting.builder()
                .isUserNotification(true) // 예시 설정 값
                .build();

        return User.builder()
                .nickname("MockUser")
                .profileImage("mock-profile-url")
                .temperature(20)
                .email("mock@example.com")
                .setting(mockSetting) // Setting 객체 설정
                .build();
    }
}


