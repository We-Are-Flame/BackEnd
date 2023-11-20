package com.backend.service.auth;


import com.backend.dto.auth.LoginResponse;
import com.backend.dto.auth.oauth2.OAuth2UserInfo;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.user.UserRepository;
import com.backend.util.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        User user = getUser(authentication);

        Date tokenCreationTime = new Date();
        String accessToken = jwtTokenProvider.sign(user, tokenCreationTime);
        response.addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));


        log.info("사용자 Access 토큰 : {} ", accessToken);

        LoginResponse resBody = LoginResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .build();

        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resBody));

        getRedirectStrategy().sendRedirect(request, response, String.format("http://localhost:3000/callback?token=%s", accessToken));
    }

    private User getUser(Authentication authentication) {
        String email = ((OAuth2UserInfo) authentication.getPrincipal()).getEmail();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NO_MATCH_EMAIL_AND_USER));
    }

}