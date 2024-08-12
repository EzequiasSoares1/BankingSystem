package com.accenture.academico.bankingsystem.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String auth;

    public void sendReceiptEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            log.debug("Preparing email to be sent to {}", to);

            helper.setFrom(auth);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            log.debug("Email prepared successfully, sending now...");

            mailSender.send(message);
            log.info("Email sent to {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
        }
    }


}
