package com.backend.service.user;

import com.backend.exception.ApiNotWorkException;
import com.backend.exception.ErrorMessages;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import freemarker.template.Configuration;
import freemarker.template.Template;


import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private static final String MAIL_ENCODING = "UTF-8";
    private static final String EMAIL_TEMPLATE = "email-template.ftl";
    private static final String FROM_EMAIL = "kitching.noreply@gmail.com";
    private static final String FROM_NAME = "Kitching";
    private static final String EMAIL_TEMPLATE_AUTH_CODE = "authCode";

    private final JavaMailSender emailSender;
    private final Configuration freemarkerConfig;

    @Async("mailExecutor")
    public void sendEmail(String toEmail, String title, String authCode) {
        try {
            MimeMessage message = createEmailForm(toEmail, title, authCode);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new ApiNotWorkException(ErrorMessages.UNABLE_TO_SEND_EMAIL);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage createEmailForm(String toEmail, String title, String authCode) throws MessagingException, IOException, TemplateException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, MAIL_ENCODING);

        Template template = freemarkerConfig.getTemplate(EMAIL_TEMPLATE);
        Map<String, Object> model = new HashMap<>();
        model.put(EMAIL_TEMPLATE_AUTH_CODE, authCode);

        StringWriter stringWriter = new StringWriter();
        template.process(model, stringWriter);
        String htmlContent = stringWriter.toString();

        message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME, MAIL_ENCODING));
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(htmlContent, true);

        return message;
    }
}