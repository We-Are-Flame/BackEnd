package com.backend.service.user;

import com.backend.exception.ApiNotWorkException;
import com.backend.exception.ErrorMessages;

import com.backend.service.amqp.EmailMessage;
import com.backend.service.amqp.EmailQueueSender;
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
        log.info("sendMessageQueue 실행");
        try {
            log.info("sendMessageQueue > createEmailForm 실행");
            EmailMessage message = createEmailForm(toEmail, title, authCode);
            log.info("sendMessageQueue > sendEmail 실행");
            emailQueueSender.sendEmail(message);
        } catch (MailException e) {
            log.info("sendMessageQueue 에러");
            throw new ApiNotWorkException(ErrorMessages.UNABLE_TO_SEND_EMAIL);
        }
        log.info("sendMessageQueue 종료");
    }

    private EmailMessage createEmailForm(String toEmail, String title, String authCode) {

        return EmailMessage.builder()
                .to(toEmail)
                .subject(title)
                .body("인증 코드 : " + authCode)
                .build();
    }
}