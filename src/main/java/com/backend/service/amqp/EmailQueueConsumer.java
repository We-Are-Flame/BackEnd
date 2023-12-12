package com.backend.service.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailQueueConsumer {

    private final JavaMailSender mailSender;
    @RabbitListener(queues = "emailQueue")
    public void receiveEmailMessage(EmailMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getBody());
        mailSender.send(mailMessage);
    }
}