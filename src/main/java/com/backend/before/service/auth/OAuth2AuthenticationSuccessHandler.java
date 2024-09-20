package com.backend.before.service.auth;


import com.backend.before.dto.auth.oauth2.OAuth2UserInfo;
import com.backend.before.dto.auth.response.LoginResponse;
import com.backend.before.entity.user.User;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.before.repository.user.UserRepository;
import com.backend.before.util.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Environment environment;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        User user = getUser(authentication);

        Date tokenCreationTime = new Date();
        String accessToken = jwtTokenProvider.sign(user, tokenCreationTime);
        response.addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));

        log.info("사용자 Access 토큰 : {} ", accessToken);
//        log.info("모킹 Access 토큰 : {} ", JwtMocking.createMockJwt(tokenCreationTime));

        LoginResponse resBody = LoginResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .build();

        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resBody));

        String callbackUrl = environment.getProperty("callback.url");
        getRedirectStrategy().sendRedirect(request, response,
                String.format("%s?token=%s", callbackUrl, accessToken));
    }

    private User getUser(Authentication authentication) {
        String email = ((OAuth2UserInfo) authentication.getPrincipal()).getEmail();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NO_MATCH_EMAIL_AND_USER));
    }
}
