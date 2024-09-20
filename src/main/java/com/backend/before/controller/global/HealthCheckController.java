package com.backend.before.controller.global;

import com.backend.before.annotation.CheckUserNotNull;
import com.backend.before.annotation.CurrentMember;
import com.backend.before.dto.auth.response.TokenValidateResponse;
import com.backend.before.entity.user.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {
    @Hidden
    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("잘 돌아감 ㅇㅇ");
    }

    @CheckUserNotNull
    @GetMapping("/token-check")
    public ResponseEntity<TokenValidateResponse> tokenValidate(@CurrentMember User user) {
        return ResponseEntity.ok(TokenValidateResponse.success(user.getId()));
    }
}
