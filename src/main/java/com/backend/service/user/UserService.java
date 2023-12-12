package com.backend.service.user;

import static com.backend.util.mapper.user.UserResponseMapper.buildMailResponse;
import static com.backend.util.mapper.user.UserResponseMapper.buildUserNotification;

import com.backend.dto.user.request.update.UserUpdateRequest;
import com.backend.dto.user.response.read.MailResponse;
import com.backend.dto.user.response.read.UserResponse;
import com.backend.entity.user.User;
import com.backend.exception.*;
import com.backend.repository.user.UserRepository;
import com.backend.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.util.mail.AuthCodeGenerator;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private static final String AUTH_MAIL_TITLE = "Kitching 학교 인증 메일";

    private final UserRepository userRepository;
    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public UserResponse.MyPage getMyPage(Long userId) {
        User user = fetchUser(userId);
        return UserResponse.MyPage.from(user);
    }

    @Transactional
    public Long updateNickname(UserUpdateRequest.Nickname request, Long userId) {
        User user = fetchUser(userId);
        user.updateNickname(request.getNickname());
        return user.getId();
    }

    @Transactional
    public Long updateProfileImage(UserUpdateRequest.ProfileImage request, Long userId) {
        User user = fetchUser(userId);
        user.updateProfileImage(request.getImageInput().getProfileImageUrl());
        return user.getId();
    }

    @Transactional
    public Long updateUserNotification(UserUpdateRequest.Notification request, Long userId) {
        User user = fetchUser(userId);
        user.updateNotification(request.getIsNotification());
        return user.getId();
    }

    public MailResponse isVerification(Long userId) {
        User user = fetchUser(userId);
        return buildMailResponse(user);
    }

    public void sendCodeToEmail(Long userId, String toEmail) {
        checkAlreadyVerified(userId);
        checkDuplicatedEmail(toEmail);
        String authCode = AuthCodeGenerator.createCode();
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(authCodeExpirationMillis));

        mailService.sendMessageQueue(toEmail, AUTH_MAIL_TITLE, authCode);
    }

    @Transactional
    public Long verifiedCode(Long userId, String email, String authCode) {
        checkDuplicatedEmail(email);

        User user = fetchUser(userId);
        String redisKey = AUTH_CODE_PREFIX + email;
        String redisAuthCode = redisService.getValues(redisKey);

        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if (!authResult) throw new BadRequestException(ErrorMessages.NO_MATCH_AUTH_CODE);

        user.updateSchoolEmail(email);
        redisService.deleteValues(redisKey);

        return user.getId();
    }

    public UserResponse.Notification getUserNotification(Long userId) {
        User user = fetchUser(userId);
        return buildUserNotification(user);
    }

    private void checkAlreadyVerified(Long userId) {
        User user = fetchUser(userId);
        if (user.getIsSchoolVerified()) {
            throw new AlreadyExistsException(ErrorMessages.ALREADY_VERIFIED_EMAIL);
        }
    }

    private void checkDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findBySchoolEmail(email);
        if (user.isPresent()) {
            throw new BadRequestException(ErrorMessages.ALREADY_CERTIFICATED_SCHOOL_EMAIL);
        }
    }

    public User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
    }
}