package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.meesageQ.SubmissionSendMessage;
import com.example.graderbackend.exception.SubmissionSendException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubmissionProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public SubmissionProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.name}") String exchangeName,
            @Value("${rabbitmq.routing.key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void sendSubmission(SubmissionSendMessage message) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            log.info("Sent submission ID {} to worker", message.getSubmissionDto().getId());
        } catch (Exception e) {
            log.error("Failed to send submission ID {} to worker: {}",
                    message.getSubmissionDto().getId(), e.getMessage(), e);
            throw new SubmissionSendException(
                    "Failed to send submission ID " + message.getSubmissionDto().getId() + " to worker",
                    e
            );
        }
    }
}