package com.backend.controller.global;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.entity.user.User;
import com.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // 메시지 알림
    @CheckUserNotNull
    @GetMapping(value = "/api/notification/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@CurrentMember User user) {
        SseEmitter emitter = notificationService.subscribe(user.getId());
        return ResponseEntity.ok(emitter);
    }
}
