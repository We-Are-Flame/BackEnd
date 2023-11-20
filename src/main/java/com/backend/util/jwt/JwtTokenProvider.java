package com.backend.util.jwt;



import java.util.*;

import com.backend.exception.ErrorMessages;
import com.backend.exception.JwtAuthenticationException;
import com.backend.service.custom.CustomUserDetailsService;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.backend.entity.user.User;

@Component
public class JwtTokenProvider {

    private String secretKey;
    private final Long ACCESS_TOKEN_EXPIRE_TIME;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    public JwtTokenProvider(Environment env) {
        secretKey = env.getProperty("jwt.secret");
        ACCESS_TOKEN_EXPIRE_TIME = Long.parseLong(env.getProperty("jwt.access-token-expire-time"));
    }

    public String sign(User user, Date now) {
        Claims claims = Jwts.claims().setSubject(user.getNickname());
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String userSpecification = String.format("%s:%s", user.getId(), "MEMBER");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(userSpecification)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userInfo = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(this::validateTokenAndGetSubject)
                .orElseThrow(() -> new JwtAuthenticationException(ErrorMessages.INVALID_PAYLOAD));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userInfo);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String validateTokenAndGetSubject(String token) {
        try {
            return validateToken(token);
        } catch (io.jsonwebtoken.JwtException e) {
            throw new JwtAuthenticationException(generateErrorMessage(e));
        }
    }

    public String validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private static String generateErrorMessage(io.jsonwebtoken.JwtException e) {
        if (e instanceof SignatureException) return "시그니처가 일치하지 않습니다!";
        if (e instanceof ExpiredJwtException) return "토큰이 만료되었습니다!";
        if (e instanceof MalformedJwtException) return "잘못된 형식의 토큰입니다!";
        if (e instanceof UnsupportedJwtException) return "지원하지 않는 토큰입니다!";
        return "요청 형식은 맞았으나 올바른 토큰이 아닙니다!\n" + e.getMessage();
    }
}