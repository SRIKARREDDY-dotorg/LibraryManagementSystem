package com.srikar.library.queue;

import com.srikar.library.activity.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class EmailConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailConsumer emailConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessEmailQueue() {
        // Arrange
        String message = "recipient@example.com|Test Subject|Test Body";
        
        // Act
        emailConsumer.processEmailQueue(message);

        // Assert
        verify(emailService).sendEmail("recipient@example.com", "Test Subject", "Test Body");
    }
}
