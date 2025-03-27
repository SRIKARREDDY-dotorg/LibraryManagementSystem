package com.srikar.library.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailToQueue(String email, String subject, String body) {
        String message = email + "|" + subject + "|" + body;
        rabbitTemplate.convertAndSend("emailQueue", message);
    }
}
