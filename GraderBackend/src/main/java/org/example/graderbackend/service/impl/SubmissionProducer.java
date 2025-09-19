/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.meesageQ.SubmissionSendMessage
 *  com.example.graderbackend.service.impl.SubmissionProducer
 *  org.springframework.amqp.rabbit.core.RabbitTemplate
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.meesageQ.SubmissionSendMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubmissionProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public SubmissionProducer(RabbitTemplate rabbitTemplate, @Value(value="${rabbitmq.exchange.name}") String exchangeName, @Value(value="${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void sendSubmission(SubmissionSendMessage message) {
        try {
            this.rabbitTemplate.convertAndSend(this.exchangeName, this.routingKey, (Object)message);
            System.out.println("Sent submission ID " + message.getSubmissionDto().getId() + " to worker");
        }
        catch (Exception e) {
            System.err.println("Failed to send submission: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

