package com.backend.util.jwt;


import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.JwtAuthenticationException;
import com.backend.service.custom.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Long ACCESS_TOKEN_EXPIRE_TIME;
    private final String secretKey;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(Environment env, CustomUserDetailsService customUserDetailsService) {
        secretKey = env.getProperty("jwt.secret");
        ACCESS_TOKEN_EXPIRE_TIME = Long.parseLong(
                Objects.requireNonNull(env.getProperty("jwt.access-token-expire-time")));
        this.customUserDetailsService = customUserDetailsService;
    }

    private static String generateErrorMessage(io.jsonwebtoken.JwtException e) {
        if (e instanceof SignatureException) {
            return "시그니처가 일치하지 않습니다!";
        }
        if (e instanceof ExpiredJwtException) {
            return "토큰이 만료되었습니다!";
        }
        if (e instanceof MalformedJwtException) {
            return "잘못된 형식의 토큰입니다!";
        }
        if (e instanceof UnsupportedJwtException) {
            return "지원하지 않는 토큰입니다!";
        }
        return "요청 형식은 맞았으나 올바른 토큰이 아닙니다!\n" + e.getMessage();
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
}
