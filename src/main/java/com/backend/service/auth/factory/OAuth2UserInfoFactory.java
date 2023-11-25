package com.backend.service.auth.factory;

import com.backend.dto.auth.oauth2.KakaoUserInfo;
import com.backend.dto.auth.oauth2.OAuth2UserInfo;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2UserInfoFactory {
    public OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        if ("kakao".equals(provider)) {
            log.info("카카오 로그인 요청");
            return new KakaoUserInfo(attributes);
        }
        throw new IllegalArgumentException("잘못된 OAuth2 공급자 입니다. : " + provider);
    }
}
