package com.backend.service.user;

import com.backend.exception.ApiNotWorkException;
import com.backend.exception.ErrorMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@RequiredArgsConstructor
@EnableAsync
public class MailService {
    private static final String FROM_EMAIL = "kitching.noreply@gmail.com";

    private final JavaMailSender emailSender;

    @Async
    public void sendEmail(String toEmail, String title, String authCode) {
        try {
            SimpleMailMessage message = createEmailForm(toEmail, title, authCode);
            emailSender.send(message);
        } catch (MailException e) {
            throw new ApiNotWorkException(ErrorMessages.UNABLE_TO_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText("인증 코드 : " + authCode);
        return message;
    }
}