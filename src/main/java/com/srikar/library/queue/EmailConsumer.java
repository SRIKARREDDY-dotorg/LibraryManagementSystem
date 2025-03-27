package com.srikar.library.queue;

import com.srikar.library.activity.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "emailQueue")
    public void processEmailQueue(String message) {
        String[] parts = message.split("\\|");
        String to = parts[0];
        String subject = parts[1];
        String body = parts[2];

        emailService.sendEmail(to, subject, body);
    }
}

