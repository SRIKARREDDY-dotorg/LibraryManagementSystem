package com.srikar.library.activity.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendEmail() {
        // Arrange
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }
}
