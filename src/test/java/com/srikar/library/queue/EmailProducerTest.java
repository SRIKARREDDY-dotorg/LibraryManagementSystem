package com.srikar.library.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EmailProducer emailProducer;

    @Test
    void testSendEmailToQueue() {
        // Arrange
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";
        String expectedMessage = to + "|" + subject + "|" + body;

        // Act
        emailProducer.sendEmailToQueue(to, subject, body);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend("emailQueue", expectedMessage);
    }

    @Test
    void testSendEmailToQueueWithNullValues() {
        // Arrange
        String to = null;
        String subject = null;
        String body = null;
        String expectedMessage = "null|null|null";

        // Act
        emailProducer.sendEmailToQueue(to, subject, body);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend("emailQueue", expectedMessage);
    }
}
