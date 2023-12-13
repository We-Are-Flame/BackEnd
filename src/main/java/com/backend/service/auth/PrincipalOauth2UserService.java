package com.backend.service.auth;


import com.backend.dto.auth.oauth2.OAuth2UserInfo;
import com.backend.entity.user.Setting;
import com.backend.entity.user.User;
import com.backend.repository.user.UserRepository;
import com.backend.service.auth.factory.OAuth2UserInfoFactory;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

    private static User createNewUser(OAuth2UserInfo oAuth2UserInfo) {
        Setting newSetting = Setting.builder()
                .isUserNotification(Boolean.TRUE)
                .build();

        return User.builder()
                .setting(newSetting)
                .nickname(oAuth2UserInfo.getName())
                .profileImage(oAuth2UserInfo.getProfileImage())
                .email(oAuth2UserInfo.getEmail())
                .build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            String provider = userRequest.getClientRegistration().getRegistrationId();
            OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(provider,
                    oAuth2User.getAttributes());
            saveUserIfNotExist(oAuth2UserInfo, provider);

            return oAuth2UserInfo;
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private void saveUserIfNotExist(OAuth2UserInfo oAuth2UserInfo, String provider) {
        Optional<User> optionalUser = userRepository.findUserByEmail(oAuth2UserInfo.getEmail());

        if (optionalUser.isEmpty()) {
            User newUser = createNewUser(oAuth2UserInfo);
            userRepository.save(newUser);
        }
    }

}
