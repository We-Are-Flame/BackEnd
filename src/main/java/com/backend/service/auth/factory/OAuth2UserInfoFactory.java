package com.backend.service.auth.factory;

import com.backend.dto.auth.oauth2.KakaoUserInfo;
import com.backend.dto.auth.oauth2.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OAuth2UserInfoFactory {
    public OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        switch (provider) {

            case "kakao" -> {
                log.info("카카오 로그인 요청");
                return new KakaoUserInfo(attributes);
            }
            default -> throw new IllegalArgumentException("잘못된 OAuth2 공급자 입니다. : " + provider);
        }
    }
}