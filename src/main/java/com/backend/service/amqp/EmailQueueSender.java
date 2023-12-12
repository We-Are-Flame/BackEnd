package com.backend.service.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailQueueSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailMessage message){
        rabbitTemplate.convertAndSend("emailQueue", message);
    }
}
