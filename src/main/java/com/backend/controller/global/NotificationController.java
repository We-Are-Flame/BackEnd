package com.backend.controller.global;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.entity.user.User;
import com.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // 메시지 알림
    @CheckUserNotNull
    @GetMapping("/api/notification/subscribe")
    public SseEmitter subscribe(@CurrentMember User user) {
        return notificationService.subscribe(user.getId());
    }
}
