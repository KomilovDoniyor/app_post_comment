package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import test.app_post_comment.dto.NotificationEmail;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${mail.credentials.login}")
    public String fromEmail;

    @Async
    void send(NotificationEmail notificationEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(notificationEmail.getReceiver());
        message.setSubject(notificationEmail.getSubject());
        message.setText(notificationEmail.getBody());

        try {
            mailSender.send(message);//
            log.info("Activation email send!!!");
        } catch (MailException exception){
            log.error("Exception while sending email, ", exception);
        }
    }
}
