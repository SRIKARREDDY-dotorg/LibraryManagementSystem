package com.srikar.library.activity.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("srikarreddypochana12@gmail.com"); // Use system email
            helper.setTo(to);  // User's email
            helper.setSubject(subject);
            helper.setText(body, true); // true for HTML content

            mailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
