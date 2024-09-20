package com.backend.before.service.user;

import com.backend.before.exception.ApiNotWorkException;
import com.backend.before.exception.ErrorMessages;

import com.backend.before.service.amqp.EmailMessage;
import com.backend.before.service.amqp.EmailQueueSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

    private final EmailQueueSender emailQueueSender;
    public void sendMessageQueue(String toEmail, String title, String authCode) {
        try {
            EmailMessage message = createEmailForm(toEmail, title, authCode);
            emailQueueSender.sendEmail(message);
        } catch (MailException e) {
            throw new ApiNotWorkException(ErrorMessages.UNABLE_TO_SEND_EMAIL);
        }
    }

    private EmailMessage createEmailForm(String toEmail, String title, String authCode) {

        return EmailMessage.builder()
                .to(toEmail)
                .subject(title)
                .body("인증 코드 : " + authCode)
                .build();
    }
}